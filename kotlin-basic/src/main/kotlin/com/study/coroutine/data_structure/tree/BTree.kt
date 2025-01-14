package com.study.com.study.coroutine.data_structure.tree


fun main() {
    val bTree = BTree<Int>(2)  // 최소 차수 2로 B-Tree 생성
    val keys = listOf(10, 20, 5, 6, 12, 30, 7, 17)

    // 여러 키를 삽입하고 출력
    keys.forEach { key ->
        println("Inserting $key")
        bTree.insert(key)
    }

    // 트리 구조 출력
    bTree.printTree()
    // 특정 키 검색 테스트
    println("Searching for 6: ${bTree.search(6) != null}")
    println("Searching for 15: ${bTree.search(15) != null}")

}

/**
 * keys: 해당 노드가 저장하는 키 리스트
 * children: 자식 노드 리스트
 * isLeaf: 리프 노드 여부를 나타내는 boolean 값
 */
class BTreeNode<K : Comparable<K>>(var isLeaf: Boolean) {
    val keys: MutableList<K> = mutableListOf()
    val children: MutableList<BTreeNode<K>> = mutableListOf()

    override fun toString(): String {
        return "keys : ${keys}, children : $children"
    }
}

/**
 *  t : 최소 차수
 */
class BTree<K : Comparable<K>>(private val t: Int) {
    // 루트 노드 설정
    private var root: BTreeNode<K> = BTreeNode(isLeaf = true)

    // Btree 의 key 를 검색하다는 메서드
    fun search(key: K, node: BTreeNode<K>? = root): BTreeNode<K>? {
        val n = node ?: return null
        var i = 0;

        // 키가 노드 내의 어느 위치에 있는 지확인
        while (i < n.keys.size && key > n.keys[i]) {
            i++
        }

        // 키를 찾은 경우 해당 노드 반환
        if (i < n.keys.size && key == n.keys[i]) {
            return n
        }

        // 리프 노드인 경우 키가 없기 때문에 Null반환
        return if (n.isLeaf) null else search(n.keys[i], node)
    }

    // 키를 트리에 삽입
    fun insert(key: K) {
        val r = root
        // 키가 가득찬 경우 분할
        if (r.keys.size == 2 * t - 1) {
            val s = BTreeNode<K>(isLeaf = false)
            root = s
            s.children.add(r)
            splitChild(s, 0, r)
            insertNonFull(s, key)
        } else {
            insertNonFull(r, key)
        }
    }

    // 가득차지 않은 노드에 키를 삽입
    private fun insertNonFull(node: BTreeNode<K>, key: K) {
        var i = node.keys.size - 1

        if (node.isLeaf) {
            // 리프 노드에 삽입 후 정렬
            node.keys.add(key)
            node.keys.sort()
        } else {
            while (i >= 0 && key < node.keys[i]) {
                i--
            }
            i++
            // 자식이 가득 찬 경우 분할 수행
            if (node.children[i].keys.size == 2 * t - 1) {
                splitChild(node, i, node.children[i])
                if (key > node.keys[i]) {
                    i++
                }
            }

            insertNonFull(node.children[i], key)
        }
    }

    private fun splitChild(parent: BTreeNode<K>, index: Int, child: BTreeNode<K>) {
        val newNode = BTreeNode<K>(child.isLeaf)

        // 중간키를 기준으로 분할, 오른쪽 노드 생성
        for (j in 0 until t - 1) {
            newNode.keys.add(child.keys.removeAt(t))
        }

        if (!child.isLeaf) {
            for (j in 0 until t) {
                newNode.children.add(child.children.removeAt(j))
            }
        }
        //부모 노드에 새로운 자식 추가
        parent.children.add(index + 1, newNode)

        // 부모 노드에 키 추가
        parent.keys.add(index, child.keys.removeAt(t - 1))
    }

    // 트리의 현재 상태를 출력하는 메서드
    fun printTree(node: BTreeNode<K> = root, level: Int = 0) {
        println("Level $level: ${node.toString()}")
        node.children.forEach { printTree(it, level + 1) }
    }
}