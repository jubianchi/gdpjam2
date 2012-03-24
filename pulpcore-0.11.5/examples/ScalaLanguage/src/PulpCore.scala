// PulpCore preamble for Scala 2.7.2
// Most code will use "import PulpCore._"
//
// Don't import Int - Scala will be confused
import pulpcore.animation.{ Property, Fixed, Bool, Color, Easing, Tween }
import pulpcore.animation.{ Animation, BindFunction }
import pulpcore.math.CoreMath._

object PulpCore {

    // Convert PulpCore properties to numbers
    implicit def Int2int(v: pulpcore.animation.Int):Int = v.get
    implicit def Fixed2double(v: Fixed):Double = v.get
    implicit def Bool2bool(v: Bool):Boolean = v.get
    implicit def Color2int(v: Color):Int = v.get

    // Add methods to PulpCore properties
    implicit def Fixed2FixedView(prop: Fixed) = new FixedView(prop)
    implicit def Int2IntView(prop: pulpcore.animation.Int) = new IntView(prop)
    implicit def Bool2BoolView(prop: Bool) = new BoolView(prop)
    implicit def Color2ColorView(prop: Color) = new ColorView(prop)

    // Add methods to Timeline
    implicit def Timeline2TimelineView(t: pulpcore.animation.Timeline) = new TimelineView(t)

    // Create a new Tween animation
    implicit def Range2TweenBuilder(v: Range) = new TweenBuilder(
        true, v.start, v.end, 1000, null, 0)
    implicit def Int2TweenBuilder(v: Int) = new TweenBuilder(
        false, 0, v, 1000, null, 0)
    implicit def Double2TweenBuilder(v: Double) = new TweenBuilder(
        false, 0, v, 1000, null, 0)
    implicit def Double2MyDouble(v: Double) = new MyDouble(v)

    // Convert a Scala function to a BindFunction
    implicit def ScalaFunction2BindFunction(f: (() => Int)) =
        new IntBindFunctionView(f)
    implicit def ScalaFunction2BindFunction(f: (() => Double)) =
        new DoubleBindFunctionView(f)
}

class MyDouble(val v: Double) {
    // This isn't the best solution because (Int to Double) is illegal
    // But we can't put to(Double) in TweenBuilder because Int already has to(Int)
    def to(toValue: Double) = new TweenBuilder(true, v, toValue, 1000, null, 0)
}

class TweenBuilder(val useFromValue: Boolean,
                   val fromValue: Double, val toValue: Double,
                   val duration: Int, val easing: Easing, val delay: Int) {

    def dur(newDuration:Int) = new TweenBuilder(useFromValue, fromValue,
                                                toValue, newDuration, easing, delay)

    def ease(newEasing:Easing) = new TweenBuilder(useFromValue, fromValue,
                                                  toValue, duration, newEasing, delay)

    def delay(newDelay:Int) = new TweenBuilder(useFromValue, fromValue,
                                               toValue, duration, easing, newDelay)

    def setBehavior(prop: Fixed) {
        if (useFromValue) {
            prop.animate(fromValue, toValue, duration, easing, delay)
        }
        else {
            prop.animateTo(toValue, duration, easing, delay)
        }
    }

    def setBehavior(prop: pulpcore.animation.Int) {
        if (useFromValue) {
            prop.animate(fromValue.toInt, toValue.toInt, duration, easing, delay)
        }
        else {
            prop.animateTo(toValue.toInt, duration, easing, delay)
        }
    }

    def toTween(prop: Property) = {
        var f = 0
        var t = 0
        if (prop.isInstanceOf[Fixed]) {
            f = if (useFromValue) toFixed(fromValue) else prop.asInstanceOf[Fixed].getAsFixed
            t = toFixed(toValue)
        }
        else if (prop.isInstanceOf[Bool]) {
            f = if (useFromValue) fromValue.toInt else if (prop.asInstanceOf[Bool].get) 1 else 0
            t = toValue.toInt
        }
        else if (prop.isInstanceOf[Color]) {
            f = if (useFromValue) fromValue.toInt else prop.asInstanceOf[Color].get
            t = toValue.toInt
        }
        else if (prop.isInstanceOf[pulpcore.animation.Int]) {
            f = if (useFromValue) fromValue.toInt else prop.asInstanceOf[pulpcore.animation.Int].get
            t = toValue.toInt
        }
        new Tween(f, t, duration, easing, delay)
    }
}

class FixedView(val prop: Fixed) extends Ordered[Double] {

    def compare(that: Double): Int = { prop.get.compare(that) }

    override def equals(that: Any): Boolean = that match {
        case that: FixedView => prop.get equals that.prop.get
        case that        => prop.get equals that
    }

    def :=(v: TweenBuilder) {
        v.setBehavior(prop)
    }

    def :=(v:Double) {
        prop.set(v)
        prop
    }

    def +=(v:Double) = {
        prop.set(prop.get + v)
        prop
    }
    def -=(v:Double) = {
        prop.set(prop.get - v)
        prop
    }
    def *=(v:Double) = {
        prop.set(prop.get * v)
        prop
    }
    def /=(v:Double) = {
        prop.set(prop.get / v)
        prop
    }
}

class IntView(val prop: pulpcore.animation.Int) extends Ordered[Double] {

    def compare(that: Double): Int = { prop.get.toDouble.compare(that) }

    override def equals(that: Any): Boolean = that match {
        case that: IntView => prop.get equals that.prop.get
        case that        => prop.get equals that
    }

    def :=(v: TweenBuilder) {
        v.setBehavior(prop)
    }

    def :=(v:Int) {
        prop.set(v)
        prop
    }

    def +=(v:Int) = {
        prop.set(prop.get + v)
        prop
    }
    def -=(v:Int) = {
        prop.set(prop.get - v)
        prop
    }
    def *=(v:Int) = {
        prop.set(prop.get * v)
        prop
    }
    def /=(v:Int) = {
        prop.set(prop.get / v)
        prop
    }
}

class BoolView(val prop: Bool) {
    def :=(b: Boolean) {
        prop.set(b)
    }
}

class ColorView(val prop: Color) {
    def :=(v: Int) {
        prop.set(v)
    }
}

object Timeline {
    def apply(animations: Tuple2[Property, TweenBuilder]*) = {
        val t = new pulpcore.animation.Timeline
        for (a <- animations) {
            t.add(a._1, a._2.toTween(a._1))
        }
        t
    }
}

class TimelineView(val t: pulpcore.animation.Timeline) {
    def add(animations: Tuple2[Property, TweenBuilder]*) = {
        for (a <- animations) {
            t.add(a._1, a._2.toTween(a._1))
        }
        t
    }
}

class IntBindFunctionView(val function: (() => Int)) extends BindFunction {
    override def f() = { function() }
}

class DoubleBindFunctionView(val function: (() => Double)) extends BindFunction {
    override def f() = { function() }
}

