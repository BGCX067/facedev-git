/*
 * Provides interface for the bridge between java and internet explorer.
 *
 *  Created on: 07.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef FD_BRIDGE_H_
#define FD_BRIDGE_H_

#include "../../com.facedev.native.common/include/fd_common.h"
#include "../../com.facedev.native.common/include/fd_set.h"
#include <vector>
#include <string>
#include "fd_ie.h"

/*
 * Signalizes that bridge state was not changed due install() call.
 */
#define FD_BRIDGE_STATE_NOT_CHANGED ((fd_uint)0)

/*
 * Signalizes that bridge was installed due install() call.
 */
#define FD_BRIDGE_STATE_INSTALLED ((fd_uint)1)

/*
 * Signalizes that bridge was updated due install() call.
 */
#define FD_BRIDGE_STATE_UPDATED ((fd_uint)2)

/**
 * Signalizes that bridge was uninstalled due uninstall() call.
 */
#define FD_BRIDGE_STATE_UNINSTALLED ((fd_uint)4)

/**
 * Signalizes that error occurs due either install() or uninstall() operation.
 */
#define FD_BRIDGE_STATE_ERROR ((fd_uint)8)

/**
 * Signalizes that bridge was activated.
 */
#define FD_BRIDGE_STATE_ACTIVATED ((fd_uint)16)

/**
 * Signalizes that bridge was inactivated.
 */
#define FD_BRIDGE_STATE_INACTIVATED ((fd_uint)16)

namespace fd {

/*
 * fd_bridge is singleton that provides integration between java and BHO.
 */
class bridge {
private:
	static fd::bridge* instance;

	set<void (*) (ieinstance*, size_t, fd_uint)>* observer;
	std::vector<ieinstance*> _registered;

	bridge(){
		observer = new set<void (*) (ieinstance*, size_t, fd_uint)>();
	}

	~bridge(){
		delete observer;
	}
public:
	static inline fd::bridge* get() {
		if (instance == fd_null) {
			instance = new bridge();
		}
		return instance;
	}

	/*
	 * Checks if bridge is installed and up to date.
	 * If it is not installed - runs installation routine and returns FD_BRIDGE_STATE_INSTALLED.
	 * If it is not up to date - runs update routine and return FD_BRIDGE_STATE_UPDATED.
	 * If bridge is up to date - returns FD_BRIDGE_STATE_NOT_CHANGED.
	 * If error occurs in the one of previous steps returns FD_BRIDGE_STATE_ERROR.
	 */
	fd_uint install();

	/*
	 * Tries to uninstall the bridge. Returns FD_BRIDGE_STATE_UNINSTALLED if succeed and FD_BRIDGE_STATE_ERROR
	 * in case error occurred due bridge uninstallation.
	 */
	fd_uint uninstall();

	/*
	 * Returns unmodifiable list of all fd::ieinstance instances registered on current moment.
	 */
	inline const std::vector<ieinstance*> list() {
		return _registered;
	}

	/*
	 * Attaches listener to watch for the instances changes.
	 * listener is the function that takes tree arguments: first one is pointer
	 * to fd::ieinstance object, second is index of instance in the instances vector
	 * and third one is resulting state (either FD_BRIDGE_STATE_ACTIVATED or FD_BRIDGE_STATE_INACTIVATED)
	 */
	inline void listen(void (*fn) (ieinstance*, size_t, fd_uint)) {
		observer->add(fn);
	}

	/*
	 * Removes previously attached listener.
	 * If listener was not attached - does nothing.
	 */
	inline void unlisten(void (*fn) (ieinstance*, size_t, fd_uint)) {
		observer->remove(fn);
	}

	/**
	 * Return name of the debugger.
	 */
	std::string name();
};

} // namespace fd


#endif /* FD_BRIDGE_H_ */
