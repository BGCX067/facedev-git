/*
 * Provides event-driven pattern implementation. Observer can register listeners and dispatch
 * incoming messages to them. The order of listeners notification is not guaranteed for this implementation.
 *
 *  Created on: 08.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef FD_OBSERVER_H_
#define FD_OBSERVER_H_

#include "fd_set.h"

template <class Message>
class fd_observer {
private:
	fd_set<void(*)(Message)> listeners;
public:
	fd_observer() {}

	~fd_observer() {}

	/*
	 * Notifies all the listeners about the message.
	 */
	void fire(Message msg) {
		fd_iterator<void(*)(Message)>* it = listeners.iterator();
		while (it->has_next()) {
			it->next()(msg);
		}
		delete it;
	}

	/*
	 * Registers listener to be notified on the message.
	 */
	void listen(void(*listener)(Message)) {
		listeners.add(listener);
	}

	/*
	 * Removes listener previously registered on this observer.
	 * If listener was not registered - does nothing.
	 */
	void unlisten(void(*listener)(Message)) {
		listeners.remove(listener);
	}
};


#endif /* FD_OBSERVER_H_ */
