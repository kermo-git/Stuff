#include <iostream>


template <class K, class V>
class RedBlackMap {
    struct Node {
        K key; V value;
        Node* parent = nullptr;
        Node* left = nullptr;
        Node* right = nullptr;
        bool color = true; // true = red, false = black


        Node(const K& k) { key = k; }
        Node(const Node& other) {
            key = other.key;
            value = other.value;
            color  = other.color;

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


        /*
         *    x                           y
         *   / \    <== right rotation   / \
         *  A   y                       x   C
         *     / \  left rotation ==>  / \
         *    B   C                   A   B
         */
        void left_rotation() {
            Node* new_root = right;

            new_root->parent = parent;
            if (parent) {
                if (this == parent->left)
                    parent->left = new_root;
                else
                    parent->right = new_root;
            }

            right = right->left;
            if (right)
                right->parent = this;

            new_root->left = this;
            parent = new_root;
        }


        void right_rotation() {
            Node* new_root = left;

            new_root->parent = parent;
            if (parent) {
                if (this == parent->left)
                    parent->left = new_root;
                else
                    parent->right = new_root;
            }

            left = left->right;
            if (left)
                left->parent = this;

            new_root->right = this;
            parent = new_root;
        }


        void print(ostream& os, Node* largest) {
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


    void insert_repair(Node* x) {
        Node* p = x->parent;

        if (!p) {
            x->color = false;
            root = x;
        }
        else if (p->color) {
            Node* g = p->parent;
            bool p_is_left = (p == g->left);
            Node* u = p_is_left ? g->right : g->left;

            if (u && u->color) {
                p->color = false;
                u->color = false;
                g->color = true;
                insert_repair(g);
            }
            else {
                bool x_is_left = (x == p->left);

                if (p_is_left && x_is_left) {
                    p->color = false;
                    g->color = true;
                    g->right_rotation();

                    if (!(p->parent))
                        root = p;
                }
                else if (p_is_left && !x_is_left) {
                    x->color = false;
                    g->color = true;
                    p->left_rotation();
                    g->right_rotation();

                    if (!(x->parent))
                        root = x;
                }
                else if (!p_is_left && !x_is_left) {
                    p->color = false;
                    g->color = true;
                    g->left_rotation();

                    if (!(p->parent))
                        root = p;
                }
                else {
                    x->color = false;
                    g->color = true;
                    p->right_rotation();
                    g->left_rotation();

                    if (!(x->parent))
                        root = x;
                }
            }
        }
    }


    V& find_or_add_key(Node* node, const K& key) {
        if (key < node->key) {
            if (node->left)
                return find_or_add_key(node->left, key);

            Node* x = new Node(key);
            node->left = x;
            x->parent = node;
            insert_repair(x);

            _size++; return x->value;
        }
        if (key > node->key) {
            if (node->right)
                return find_or_add_key(node->right, key);

            Node* x = new Node(key);
            node->right = x;
            x->parent = node;
            insert_repair(x);

            _size++; return x->value;
        }
        return node->value;
    }


public:
    RedBlackMap() {}
    RedBlackMap(const RedBlackMap<K, V>& other) {
        root = (other.root)? new Node(*(other.root)) : nullptr;
        _size = other._size;
    }
    ~RedBlackMap() {
        if (root)
            delete root;
    }


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
        if (root)
            return find_or_add_key(root, key);
        root = new Node(key);
        root->color = false;
        _size++; return root->value;
    }
    void erase(const K& key) {
        // TODO
    }
    bool contains_key(const K& key) {
        return (bool) search(root, key);
    }


    friend ostream& operator<<(ostream& os, RedBlackMap& map) {
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
