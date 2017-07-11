/**
 * Created by Lazysoul on 2017. 7. 11..
 */

sealed class Tree<out A : Int>

object Empty : Tree<Nothing>()
data class Node<out A : Int>(val value: A, val left: Tree<A>,
        val right: Tree<A>) : Tree<A>()


object MyTree {

    @JvmStatic
    fun main(args: Array<String>) {

        val myTree = Empty.insertTree(5).insertTree(8).insertTree(3)

        require(myTree.contains(5))
        require(myTree.contains(8))
        require(myTree.contains(3))
        require(!myTree.contains(10))

    }

    fun <A : Int> Tree<A>.insertTree(value: A): Tree<A> {
        return when (this) {
            Empty -> Node(value, Empty, Empty)
            is Node -> {
                if (this.value == value) {
                    this
                } else {
                    if (this.value < value) {
                        Node(this.value, this.left.insertTree(value), this.right)
                    } else {
                        Node(this.value, this.left, this.right.insertTree(value))
                    }
                }
            }
        }
    }

    fun <A : Int> Tree<A>.contains(value: A): Boolean {
        return when (this) {
            Empty -> false
            is Node -> {
                if (this.value == value) {
                    true
                } else {
                    if (this.value < value) {
                        this.left.contains(value)
                    } else {
                        this.right.contains(value)
                    }
                }
            }
        }
    }

}
