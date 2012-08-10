/*
 * Common collections utilities and classes.
 *
 *  Created on: 10.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef FD_COLLECTION_H_
#define FD_COLLECTION_H_


/*
 * Error that is thrown when no element found.
 */
template <class E>
class fd_no_such_element {
private:
	E _el;
public:
	fd_no_such_element() {
	}

	fd_no_such_element(E el) {
		_el = el;
	}

	inline E element() {
		return _el;
	}
};


template <class E>
class fd_iterator {
public:
	fd_iterator() {}
	virtual ~fd_iterator() {}

	virtual E next() = 0;
	virtual bool has_next() = 0;
};


#endif /* FD_COLLECTION_H_ */
