#ifndef AVLMAP_H
#define AVLMAP_H
#include <iostream>


template <class K, class V>
class AVLMap {
    struct Node {
        K key; V value;
        Node* left = nullptr;
        Node* right = nullptr;
        int height = 1;


        Node(const K& k) { key = k; }
        Node(const Node& other) {
            key = other.key;
            value = other.value;

            if (other.left)
                left = new Node(*(other.left));
            if (other.right)
                right = new Node(*(other.right));
        }
        ~Node() {
            if (left)
                delete left;
            if (right)
                delete right;
        }


        void updateHeight() {
            int l_height = (left)? left->height : 0;
            int r_height = (right)? right->height : 0;
            height = (l_height > r_height)? 1 + l_height : 1 + r_height;
        }
        int balanceFactor() {
            int l_height = (left)? left->height : 0;
            int r_height = (right)? right->height : 0;
            return l_height - r_height;
        }


        /*
         *    x                           y
         *   / \    <== right rotation   / \
         *  A   y                       x   C
         *     / \  left rotation ==>  / \
         *    B   C                   A   B
         */
        Node* leftRotation() {
            Node* y = right;
            right = right->left;
            y->left = this;

            updateHeight();
            y->updateHeight();
            return y;
        }
        Node* rightRotation() {
            Node* y = left;
            left = left->right;
            y->right = this;

            updateHeight();
            y->updateHeight();
            return y;
        }
        Node* rightLeftRotation() {
            right = right->rightRotation();
            return leftRotation();
        }
        Node* leftRightRotation() {
            left = left->leftRotation();
            return rightRotation();
        }


        Node* balance() {
            int balancefactor = balanceFactor();

            if (balancefactor == 2) {
                if (left->balanceFactor() == -1)
                    return leftRightRotation();
                return rightRotation();
            }
            else if (balancefactor == -2) {
                if (right->balanceFactor() == 1)
                    return rightLeftRotation();
                return leftRotation();
            }
            updateHeight();
            return this;
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


    Node* search(Node* node, const K& key) {
        if (node) {
            if (key < node->key)
                return search(node->left, key);
            if (key > node->key)
                return search(node->right, key);
        }
        return node;
    }


    Node* temp;
    Node* findOrCreateKey(Node* node, const K& key) {
        if (!node) {
            _size++;
            return temp = new Node(key);
        }
        if (key < node->key)
            node->left = findOrCreateKey(node->left, key);
        else if (key > node->key)
            node->right = findOrCreateKey(node->right, key);
        else
            return temp = node;
        return node->balance();
    }


    Node* removeKey(Node* node, const K& key) {
        if (!node)
            return nullptr;
        if (key < node->key)
            node->left = removeKey(node->left, key);
        else if (key > node->key)
            node->right = removeKey(node->right, key);
        else {
            if (!(node->left)) {
                _size--;
                Node* tmp = node->right;
                node->right = nullptr;
                delete node; return tmp;
            }
            else if (!(node->right)) {
                _size--;
                Node* tmp = node->left;
                node->left = nullptr;
                delete node; return tmp;
            }
            else {
                Node* next = node->right;
                while (next->left)
                    next = next->left;

                node->key = next->key;
                node->value = next->value;
                node->right = removeKey(node->right, node->key);
            }
        }
        return node->balance();
    }


public:
    AVLMap() {}
    AVLMap(const AVLMap<K, V>& other) {
        root = (other.root)? new Node(*(other.root)) : nullptr;
        _size = other._size;
    }
    ~AVLMap() { if (root) delete root; }


    int size() { return _size; }
    bool empty() { return _size == 0; }
    void clear() {
        if (root)
            delete root;
        root = nullptr;
        _size = 0;
    }


    V& at(const K& key) {
        Node *node = search(root, key);
        if (node)
            return node->value;
        throw std::runtime_error("No such key.");
    }
    V& operator[](const K& key) {
        root = findOrCreateKey(root, key);
        return temp->value;
    }
    void erase(const K& key) {
        root = removeKey(root, key);
    }
    bool contains(const K& key) {
        return (bool) search(root, key);
    }


    friend std::ostream& operator<<(std::ostream& os, const AVLMap& map) {
        os << "{";
        if (map.root) {
            AVLMap::Node *largest = map.root;
            while (largest->right)
                largest = largest->right;
            map.root->print(os, largest);
        }
        os << "}";
        return os;
    }
};

#endif
