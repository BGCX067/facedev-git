/*
 * Provides map/dictionary functionality. This implementation uses hashtable to manage keys.
 * Keys should provide == operator implementation. Default hash function assumes that pointers
 * are used as keys and uses memory address as hash value. If you want to provide custom
 * hash function - you may pass it to the constructor.
 *
 * Hashtable implements sequences collision resolving algorithm and uses 0.75 load factor to rehash.
 *
 *  Created on: 09.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef FD_MAP_H_
#define FD_MAP_H_

#include "fd_lang.h"
#include "fd_collection.h"
#include <string.h>

/*
 * Template for map.
 */
template <class K, class V>
class fd_map {
private:
	class Entry {
	public:
		Entry* next;
		K key;
		V value;
		size_t hash;

		Entry(K key, V value, size_t hash){
			this->next = fd_null;
			this->key = key;
			this->value = value;
			this->hash = hash;
		}
		~Entry(){}
	};

	Entry** map;
	size_t length;
	size_t count;
	size_t (*hash)(K);

	static size_t default_hash(K el) {
		register size_t key;
		key = (size_t) el;
		return (key >> 3U) * 2654435761U;
	}

	void rehash(size_t newSize) {
		Entry** old = this->map;
		this->map = new Entry*[newSize];
		memset(this->map, 0, this->length * sizeof(Entry*));

		for (size_t i = 0; i < length; i++) {
			Entry* entry = old[i];

			while (entry != fd_null) {
				Entry* next = entry->next;

				entry->next = fd_null;
				size_t hc = entry->hash % newSize;
				if (this->map[hc] == fd_null) {
					this->map[hc] = entry;
				} else {
					Entry* parent = this->map[hc];
					while (parent->next != fd_null) {
						parent = parent->next;
					}
					parent->next = entry;
				}

				entry = next;
			}
		}
		delete[] old;
		this->length = newSize;
	}

	Entry* get_entry(K key) {
		size_t hc = hash(key) % length;
		Entry* entry = map[hc];
		while (entry != fd_null) {
			if (entry->key == key) {
				return entry;
			}
			entry = entry->next;
		}
		return fd_null;
	}

public:
	/*
	 * Creates map using default size (cannot be less than 10)
	 * and hash function to create fast hashes.
	 */
	fd_map(size_t initialSize = 10, size_t (*hash)(K) = fd_null) {
		this->hash = hash == fd_null ? default_hash : hash;
		this->length = initialSize < 10 ? 10 : initialSize;
		this->map = new Entry*[this->length];
		memset(this->map, 0, this->length * sizeof(Entry*));
		this->count = 0;
	}

	~fd_map() {
		delete[] this->map;
	}

	/*
	 * Returns true if this map has key specified and false otherwise.
	 */
	inline bool has(K key) {
		return get_entry(key) != fd_null;
	}

	/*
	 * Puts new value in the map associated with key. If old association exists - it will be removed.
	 */
	inline void put(K key, V value) {
		if ( (count * 4) > (length * 3) ) {
			rehash(length * 2);
		}
		size_t real_hc = hash(key);
		size_t hc = real_hc % length;
		Entry* entry = map[hc];
		Entry* parent = fd_null;
		while (entry != fd_null) {
			if (entry->key == key) {
				entry->value = value;
				return;
			}
			parent = entry;
			entry = entry->next;
		}

		Entry* newone = new Entry(key, value, real_hc);
		count++;

		if (parent == fd_null) {
			map[hc] = newone;
		} else {
			parent->next = newone;
		}
	}

	/*
	 * Gets (returns) association for key or throws fd_no_such_key error if there is no such a key
	 */
	inline V get(K key) {
		Entry* entry = get_entry(key);
		if (entry == fd_null) {
			throw fd_no_such_element<K>(key);
		}
		return entry->value;
	}

	/*
	 * Removes association for key or throws fd_no_such_key error if there is no such a key.
	 * Returns value previously associated with key.
	 */
	V remove(K key) {
		if ( (count * 8) < length && length > 10) {// TODO: check rehash
			rehash(count * 2);
		}
		size_t hc = hash(key) % length;
		Entry* entry = map[hc];
		Entry* parent = fd_null;

		while (entry != fd_null) {
			if (entry->key == key) {
				if (parent == fd_null) {
					map[hc] = entry->next;
				} else {
					parent->next = entry->next;
				}
				V value = entry->value;
				delete entry;
				count--;
				return value;
			}
			parent = entry;
			entry = entry->next;
		}

		throw fd_no_such_element<K>(key);
	}

	/*
	 * Returns current map size.
	 */
	inline size_t size() {
		return count;
	}

	/*
	 * Returns array of values of size #size(). Order of values is not determined by this implementation.
	 * Array is dynamically allocated and should be released by client.
	 */
	V* values() {
		V* result = new V[count];

		register int i = 0;

		for (size_t x = 0; x < length; x++) {
			Entry* entry = map[x];

			while (entry != fd_null) {
				result[i++] = entry->value;
				entry = entry->next;
			}
		}

		return result;
	}

	/*
	 * Returns array of keys of size #size(). Order of keys is not determined by this implementation.
	 * Array is dynamically allocated and should be released by client.
	 */
	K* keys() {
		K* result = new K[count];

		register size_t i = 0;

		for (size_t x = 0; x < length; x++) {
			Entry* entry = map[x];

			while (entry != fd_null) {
				result[i++] = entry->key;
				entry = entry->next;
			}
		}

		return result;
	}
};


#endif /* FD_MAP_H_ */
