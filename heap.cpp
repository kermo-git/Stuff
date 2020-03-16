#include <iostream>
using namespace std;

template <class E>
class Heap {
protected:
    int _size = 0;
    int capacity = 10;
    E* tree = new E[capacity];


    void resize(int new_capacity) {
        capacity = new_capacity;
        E* new_tree = new E[capacity];
        for (int i = 0; i < _size; i++) {
            new_tree[i] = tree[i];
        }
        delete[] tree;
        tree = new_tree;
    }
    void grow() {
        if (_size >= capacity)
            resize(capacity << 1);
    }
    void shrink() {
        if (_size <= capacity >> 2)
            resize(capacity >> 1);
    }


    virtual bool wrong_order(int parent, int child)=0;
    virtual int choose_next(int left, int right)=0;


    void downheap(int k) {
        int l_child = 2*k + 1;
        int r_child = 2*k + 2;

        if (r_child < _size) {
            if (wrong_order(k, l_child) ||
                wrong_order(k, r_child)) {

                int next = choose_next(l_child, r_child);
                swap(k, next); downheap(next);
            }
        } else if (l_child < _size && wrong_order(k, l_child)) {
            swap(k, l_child); downheap(l_child);
        }
    }


    void upheap(int k) {
        int parent = (k - 1) >> 1;
        if (parent >= 0 && wrong_order(parent, k)) {
            swap(parent, k); upheap(parent);
        }
    }


    void swap(int index_1, int index_2) {
        E tmp = tree[index_1];
        tree[index_1] = tree[index_2];
        tree[index_2] = tmp;
    }


public:
    ~Heap() { delete[] tree; }
    bool empty() { return _size == 0;}

    E peek() { return tree[0]; }
    E pop() {
        if (_size == 0) {
            delete[] tree;
            throw runtime_error("Empty heap");
        }
        E root = tree[0];
        shrink();
        tree[0] = tree[_size - 1];
        _size--;
        downheap(0);
        return root;
    }
    void push(const E& item) {
        grow();
        tree[_size] = item;
        _size++;
        upheap(_size - 1);
    }
};


template <class E>
class MaxHeap: public Heap<E> {
    bool wrong_order(int parent, int child) {
        return this->tree[parent] < this->tree[child];
    };
    int choose_next(int left, int right) {
        return (this->tree[left] > this->tree[right])? left : right;
    };
};


template <class E>
class MinHeap: public Heap<E> {
    bool wrong_order(int parent, int child) {
        return this->tree[parent] > this->tree[child];
    };
    int choose_next(int left, int right) {
        return (this->tree[left] < this->tree[right])? left : right;
    };
};


template <class E>
class Stack {
    struct Node {
        E item;
        Node* next;

        Node(const E& new_item, Node* next_node) {
            item = new_item;
            next = next_node;
        }
    };
    void check() {
        if (_size == 0) {
            clear();
            throw runtime_error("Empty stack");
        }
    }
    Node* head = NULL;
    int _size = 0;
public:
    ~Stack() { clear(); }
    void clear() {
        _size = 0; Node* tmp;
        while (head) {
            tmp = head;
            head = head->next;
            delete tmp;
        }
    }
    int size() { return _size; }
    bool empty() { return _size == 0; }
    void push(const E& item) {
        head = new Node(item, head);
        _size++;
    }
    E peek() {
        check();
        return head->item;
    }
    E pop() {
        check();
        E item = head->item;
        Node* tmp = head;
        head = head->next;
        delete tmp;
        _size--;
        return item;
    }
};


template <class E>
class Queue {
    struct Node {
        E item;
        Node* next = NULL;
        Node(const E& new_item) {
            item = new_item;
        }
    };
    void check() {
        if (_size == 0) {
            clear();
            throw runtime_error("Empty queue");
        }
    }
    Node* head = NULL;
    Node* tail = NULL;
    int _size = 0;
public:
    ~Queue() { clear(); }
    void clear() {
        _size = 0; Node* tmp;
        while (head) {
            tmp = head;
            head = head->next;
            delete tmp;
        }
    }
    int size() { return _size; }
    bool empty() { return _size == 0; }
    void push(const E& item) {
        if (_size == 0) {
            head = new Node(item);
            tail = head;
        } else {
            tail->next = new Node(item);
            tail = tail->next;
        }
        _size++;
    }
    E peek() {
        check();
        return head->item;
    }
    E pop() {
        check();
        E item = head->item;
        Node* tmp = head;
        head = head->next;
        delete tmp;
        _size--;
        return item;
    }
};


int heap_main() {
    MinHeap<int> heap;
    heap.push(21);
    heap.push(19);
    heap.push(24);
    heap.push(15);
    heap.push(42);
    heap.push(34);
    heap.push(31);
    heap.push(9);
    heap.push(53);
    heap.push(49);
    heap.push(0);
    heap.push(27);
    heap.push(76);
    heap.push(17);
    heap.push(22);
    heap.push(47);
    heap.push(7);
    heap.push(23);
    heap.push(11);
    heap.push(12);
    heap.push(13);
    heap.push(10);
    heap.push(74);
    heap.push(8);
    heap.push(5);
    heap.push(3);
    heap.push(55);
    heap.push(4);
    heap.push(18);
    heap.push(1);
    heap.push(65);

    while(!heap.empty()) {
        cout << heap.pop() << " ";
    }
    cout << endl;

    return 0;
}
