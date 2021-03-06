/*
 * Hash-based implementation of set.
 *
 *  Created on: 10.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef FD_SET_H_
#define FD_SET_H_

#include "fd_lang.h"
#include "fd_map.h"

namespace fd {

template <class E>
class set {
private:
	template <class T>
	class array_iterator : public fd::iterator<T> {
	private:
		T* array;
		size_t size;
		size_t index;
	public:
		array_iterator(T* array, size_t size) {
			this->array = array;
			this->size = size;
			this->index = 0;
		}

		virtual ~array_iterator() {
			delete[] this->array;
		}

		virtual T next() {
			if (index >= size) {
				throw no_such_element<T>();
			}
			return array[index++];
		}

		virtual bool has_next() {
			return index < size;
		}
	};

	fd::map<E, int>* map;
public:
	/*
	 * Creates set based on initial size (cannot be less than 10) and
	 * hash function (default hash function works with pointers and uses memory address).
	 */
	set(int initialSize = 10, size_t (*hash)(E) = fd_null) {
		map = new fd::map<E, int>(initialSize, hash);
	}

	~set() {
		delete map;
	}

	inline void add(E el) {
		map->put(el, 1);
	}

	inline fd::iterator<E>* iterator() {
		return new array_iterator<E>(map->keys(), map->size());
	}

	inline bool contains(E el) {
		return map->has(el);
	}

	inline void remove(E el) {
		if (map->has(el)) {
			map->remove(el);
		}
	}

	inline size_t size() {
		return map->size();
	}
};

} // namespace fd {

#endif /* FD_SET_H_ */
