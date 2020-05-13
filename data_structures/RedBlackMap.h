#ifndef REDBLACKMAP_H
#define REDBLACKMAP_H
#include <iostream>


#define RED true
#define BLACK false


/*
 * A red-black tree based implementation of a key-value map (just like Java TreeMap).
 * All operation are O(log n). Public methods are similar to C++'s std::map:
 * 
 * int size()
 * bool empty()
 * void clear()
 * V& at(const K& key) - return the value associated with the key or throws an exception if it doesn't exist
 * V& operator[](const K& key) - can be used in two ways:
 *   map[5] = "value" // adding a new key or overwriting the value for an existing key.
 *   string str = map[5] // get a value by key. If it doesn't exist, it creates a new key with a default value.
 * void erase(const K& key) - removes a key, if it exists.
 * bool contains(const K& key)
 */
template <class K, class V>
class RedBlackMap {
    struct Node {
        K key; V value;
        Node* parent = nullptr;
        Node* left = nullptr;
        Node* right = nullptr;
        bool color = RED;


        Node(const K& k) { key = k; }
        Node(const Node& other) {
            key = other.key;
            value = other.value;
            color = other.color;

            if (other.left) {
                left = new Node(*(other.left));
                left->parent = this;
            }
            if (other.right) {
                right = new Node(*(other.right));
                right->parent = this;
            }
        }
        ~Node() {
            if (left)
                delete left;
            if (right)
                delete right;
        }


        bool isOnLeft() {
            return this == parent->left;
        }
        Node* sibling() {
            return (parent) ?
                   (isOnLeft() ? parent->right : parent->left ) : nullptr;
        }
        Node* grandparent() {
            return (parent)? parent->parent : nullptr;
        }
        Node* uncle() {
            Node* g = grandparent();
            return g ? ((parent->isOnLeft()) ? g->right : g->left) : nullptr;
        }


        void print(std::ostream& os, Node* largest) {
            if (left)
                left->print(os, largest);
            if (this == largest) {
                os << key << "=" << value;
                return;
            }
            os << key << "=" << value << ", ";
            if (right)
                right->print(os, largest);
        }
    };


    Node* root = nullptr;
    int _size = 0;


    /*
     *    x                           y
     *   / \    <== right rotation   / \
     *  A   y                       x   C
     *     / \  left rotation ==>  / \
     *    B   C                   A   B
     */
    void leftRotation(Node* x) {
        Node* y = x->right;
        x->right = y->left;
        y->left = x;

        if (x->right)
            x->right->parent = x;

        y->parent = x->parent;
        if (x != root) {
            if (x->isOnLeft())
                x->parent->left = y;
            else
                x->parent->right = y;
        }
        else
            root = y;
        x->parent = y;
    }
    void rightRotation(Node* x) {
        Node* y = x->left;
        x->left = y->right;
        y->right = x;

        if (x->left)
            x->left->parent = x;

        y->parent = x->parent;
        if (x != root) {
            if (x->isOnLeft())
                x->parent->left = y;
            else
                x->parent->right = y;
        }
        else
            root = y;
        x->parent = y;
    }


    void fixRedRed(Node* x) {
        if (x == root)
            x->color = BLACK;
        else if (x->parent->color == RED) {
            Node* parent = x->parent;
            Node* grandparent = parent->parent;
            Node* uncle = x->uncle();

            grandparent->color = RED;

            if (uncle && uncle->color == RED) {
                parent->color = BLACK;
                uncle->color = BLACK;
                fixRedRed(grandparent);
            }
            else {
                if (parent->isOnLeft()) {
                    if (x->isOnLeft())
                        parent->color = BLACK;
                    else {
                        x->color = BLACK;
                        leftRotation(parent);
                    }
                    rightRotation(grandparent);
                } else {
                    if (x->isOnLeft()) {
                        x->color = BLACK;
                        rightRotation(parent);
                    }
                    else
                        parent->color = BLACK;
                    leftRotation(grandparent);
                }
            }
        }
    }


    void fixDoubleBlack(Node* parent, Node* sibling) {
        if (!parent)
            return;
        if (!sibling)
            fixDoubleBlack(parent->parent, parent->sibling());
        else {
            if (sibling->color == RED) {
                parent->color = RED;
                sibling->color = BLACK;
                if (sibling->isOnLeft()) {
                    rightRotation(parent);
                    fixDoubleBlack(parent, parent->left);
                } else {
                    leftRotation(parent);
                    fixDoubleBlack(parent, parent->right);
                }
            }
            else {
                bool leftRed = sibling->left && sibling->left->color == RED;
                bool rightRed = sibling->right && sibling->right->color == RED;

                if (!leftRed && !rightRed) {
                    sibling->color = RED;
                    if (parent->color == BLACK)
                        fixDoubleBlack(parent->parent, parent->sibling());
                }
                else if (sibling->isOnLeft()) {
                    if (leftRed) {
                        sibling->left->color = BLACK;
                        sibling->color = parent->color;
                        rightRotation(parent);
                    } else {
                        sibling->right->color = parent->color;
                        leftRotation(sibling);
                        rightRotation(parent);
                    }
                } else {
                    if (rightRed) {
                        sibling->right->color = BLACK;
                        sibling->color = parent->color;
                        leftRotation(parent);
                    } else {
                        sibling->left->color = parent->color;
                        rightRotation(sibling);
                        leftRotation(parent);
                    }
                }
                parent->color = BLACK;
            }
        }
    }


    Node* search(Node* node, const K& key) {
        if (node) {
            if (key < node->key)
                return search(node->left, key);
            if (key > node->key)
                return search(node->right, key);
        }
        return node;
    }


    V& findOrCreateKey(Node* node, const K& key) {
        if (key < node->key) {
            if (node->left)
                return findOrCreateKey(node->left, key);

            Node* x = new Node(key);
            node->left = x;
            x->parent = node;
            fixRedRed(x);

            _size++; return x->value;
        }
        if (key > node->key) {
            if (node->right)
                return findOrCreateKey(node->right, key);

            Node* x = new Node(key);
            node->right = x;
            x->parent = node;
            fixRedRed(x);

            _size++; return x->value;
        }
        return node->value;
    }


    void remove(Node* v) {
        if (v->left && v->right) {
            Node* u = v->right;
            while (u->left)
                u = u->left;

            K u_key = u->key;
            V u_value = u->value;

            u->key = v->key;
            u->value = v->value;

            v->key = u_key;
            v->value = u_value;

            remove(u);
        }
        else {
            Node* u = nullptr;
            if (v->left) {
                u = v->left; v->left = nullptr;
            }
            else if (v->right) {
                u = v->right; v->right = nullptr;
            }

            if (v == root) {
                root = u;
            } else {
                Node* parent = v->parent;
                Node* sibling = v->sibling();

                if (v->isOnLeft())
                    parent->left = u;
                else
                    parent->right = u;

                if (u) {
                    u->parent = v->parent;
                    if (v->color == RED || u->color == RED) {
                        u->color = BLACK;
                    } else {
                        fixDoubleBlack(parent, sibling);
                    }
                } else if (v->color == BLACK) {
                    fixDoubleBlack(parent, sibling);
                }
            }
            delete v;
        }
    }


public:
    RedBlackMap() {}
    RedBlackMap(const RedBlackMap<K, V>& other) {
        root = (other.root)? new Node(*(other.root)) : nullptr;
        _size = other._size;
    }
    ~RedBlackMap() { if (root) delete root; }


    int size() { return _size; }
    bool empty() { return _size == 0; }
    void clear() {
        if (root)
            delete root;
        root = nullptr;
        _size = 0;
    }


    V& at(const K& key) {
        Node* node = search(root, key);
        if (node)
            return node->value;
        throw std::runtime_error("No such key.");
    }
    V& operator[](const K& key) {
        if (root)
            return findOrCreateKey(root, key);
        root = new Node(key);
        root->color = BLACK;
        _size++; return root->value;
    }
    void erase(const K& key) {
        Node* node = search(root, key);
        if (node) {
            remove(node); _size--;
        }
    }
    bool contains(const K& key) {
        return (bool) search(root, key);
    }


    friend std::ostream& operator<<(std::ostream& os, const RedBlackMap& map) {
        os << "{";
        if (map.root) {
            RedBlackMap::Node* largest = map.root;
            while (largest->right)
                largest = largest->right;
            map.root->print(os, largest);
        }
        os << "}";
        return os;
    }
};

#endif
