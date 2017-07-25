sealed class List<out A>
object Nil : List<Nothing>()
data class Cons<out A>(val head: A, val tail: List<A>) : List<A>()

object MyList {

    @JvmStatic
    fun main(args: Array<String>) {
        val ints = Cons(1, Cons(2, Cons(3, Cons(4, Cons(5, Nil)))))
        val doubles = Cons(1.0, Cons(2.0, Cons(3.0, Cons(4.0, Cons(5.0, Nil)))))
        val strings = Cons("A", Cons("B", Cons("C", Cons("D", Cons("E", Nil)))))

        require(sum(ints) == 15)
        require(product(doubles) == 120.0)
        require(getHead(ints) == 1)
        require(getTail(ints) == Cons(2, Cons(3, Cons(4, Cons(5, Nil)))))
        require(drop(3, ints) == Cons(4, Cons(5, Nil)))
        require(dropWhile(ints, { it < 4 }) == Cons(4, Cons(5, Nil)))
        require(foldRight(ints, 0, { x, y -> x + y }) == 15)
        require(foldRight(strings, "", { x, y -> x + y }) == "ABCDE")
        require(foldLeft(ints, 0, { x, y -> x + y }) == 15)
        require(foldLeft(strings, "", { x, y -> x + y }) == "ABCDE")
        require(reverse(ints) == Cons(5, Cons(4, Cons(3, Cons(2, Cons(1, Nil))))))
        require(append(Cons(1, Cons(2, Cons(3, Nil))), Cons(4, Cons(5, Nil)))
                == Cons(1, Cons(2, Cons(3, Cons(4, Cons(5, Nil))))))
        require(plus1(ints) == Cons(2, Cons(3, Cons(4, Cons(5, Cons(6, Nil))))))
        require(doubleToString(doubles) == Cons("1.0",
                Cons("2.0", Cons("3.0", Cons("4.0", Cons("5.0", Nil))))))
        require(map(ints, { it + 1 }) == Cons(2, Cons(3, Cons(4, Cons(5, Cons(6, Nil))))))
        require(filter(ints, { it % 2 == 0 }) == Cons(2, Cons(4, Nil)))
        require(flatMap(ints, { Cons(it, Cons(it + 1, Nil)) }) == Cons(1, Cons(2,
                Cons(2, Cons(3, Cons(3, Cons(4, Cons(4, Cons(5, Cons(5, Cons(6, Nil)))))))))))
    }

    fun sum(ints: List<Int>): Int = when (ints) {
        is Nil -> 0
        is Cons -> ints.head + sum(ints.tail)
    }

    fun product(ds: List<Double>): Double = when (ds) {
        is Nil -> 1.0
        is Cons -> if (ds.head == 0.0) {
            0.0
        } else {
            ds.head * product(ds.tail)
        }
    }

    fun <A> getHead(list: List<A>): A = when (list) {
        is Nil -> throw IllegalArgumentException()
        is Cons<A> -> list.head
    }

    fun <A> getTail(list: List<A>): List<A> = when (list) {
        is Nil -> Nil
        is Cons<A> -> list.tail
    }

    fun <A> drop(n: Int, list: List<A>): List<A> = when (n) {
        0 -> list
        else -> drop(n - 1, getTail(list))
    }

    fun <A> dropWhile(list: List<A>, f: (A) -> Boolean): List<A> = when (list) {
        is Nil -> Nil
        is Cons -> if (f(list.head)) {
            dropWhile(list.tail, f)
        } else {
            list
        }
    }

    fun <A, B> foldRight(list: List<A>, z: B, f: (A, B) -> B): B = when (list) {
        is Nil -> z
//        is Cons -> foldRight(list.tail, f(list.head, z), f)
        is Cons -> f(list.head, foldRight(list.tail, z, f))
    }

    fun <A, B> foldLeft(list: List<A>, z: B, f: (B, A) -> B): B = when (list) {
        is Nil -> z
        is Cons -> foldLeft(list.tail, f(z, list.head), f)
    }

    fun <A> reverse(list: List<A>): List<A> = foldLeft(list, Nil as List<A>, { x, y -> Cons(y, x) })

    fun <A> append(listA: List<A>, listB: List<A>): List<A> = foldRight(listA, listB,
            { x, acc -> Cons(x, acc) })

    fun plus1(list: List<Int>): List<Int> = when (list) {
        is Nil -> Nil
        is Cons<Int> -> Cons(list.head + 1, plus1(list.tail))
    }

    fun doubleToString(list: List<Double>): List<String> = when (list) {
        is Nil -> Nil
        is Cons<Double> -> Cons(list.head.toString(), doubleToString(list.tail))
    }

    fun <A, B> map(list: List<A>, f: (A) -> B): List<B> = when (list) {
        is Nil -> Nil
        is Cons<A> -> Cons(f(list.head), map(list.tail, f))
    }

    fun <A> filter(list: List<A>, f: (A) -> Boolean): List<A> = when (list) {
        is Nil -> Nil
        is Cons<A> -> if (f(list.head)) {
            Cons(list.head, filter(list.tail, f))
        } else {
            filter(list.tail, f)
        }
    }

    fun <A, B> flatMap(list: List<A>, f: (A) -> List<B>): List<B> = when (list) {
        is Nil -> Nil
        is Cons<A> -> append(f(list.head), flatMap(list.tail, f))
    }
}