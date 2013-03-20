/*
 * Provides interface for the bridge between java, BHO and internet explorer.
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
#include <guiddef.h>
#include "fd_ie.h"

/*
 * Class (CLSID) id for BHO. Used for BHO registration and accessing.
 */
// {82E196FE-EF7C-4d76-B64E-C4EA605A3356}
extern const GUID FACEDEV_IE_BHO_GUID;


/*
 * Signalizes that BHO state was not changed due install() call.
 */
#define FD_BHO_STATE_NOT_CHANGED ((fd_uint)0)

/*
 * Signalizes that BHO was installed due install() call.
 */
#define FD_BHO_STATE_INSTALLED ((fd_uint)1)

/*
 * Signalizes that BHO was updated due install() call.
 */
#define FD_BHO_STATE_UPDATED ((fd_uint)2)

/**
 * Signalizes that BHO was uninstalled due uninstall() call.
 */
#define FD_BHO_STATE_UNINSTALLED ((fd_uint)4)

/**
 * Signalizes that error occurs due either install() or uninstall() operation.
 */
#define FD_BHO_STATE_ERROR ((fd_uint)8)

/**
 * Signalizes that BHO was activated.
 */
#define FD_BHO_STATE_ACTIVATED ((fd_uint)16)

/**
 * Signalizes that BHO was inactivated.
 */
#define FD_BHO_STATE_INACTIVATED ((fd_uint)16)

/*
 * fd_bridge is singleton that provides integration between java and BHO.
 */
class fd_bridge {
private:
	static fd_bridge* instance;

	fd_set<void (*) (fd_ieinstance*, size_t, fd_uint)>* observer;
	std::vector<fd_ieinstance*> _registered;

	fd_bridge(){
		observer = new fd_set<void (*) (fd_ieinstance*, size_t, fd_uint)>();
	}

	~fd_bridge(){
		delete observer;
	}
public:
	static inline fd_bridge* get() {
		if (instance == fd_null) {
			instance = new fd_bridge();
		}
		return instance;
	}

	/*
	 * Checks if BHO is installed and up to date.
	 * If it is not installed - runs installation routine and returns FD_BHO_STATE_INSTALLED.
	 * If it is not up to date - runs update routine and return FD_BHO_STATE_UPDATED.
	 * If BHO is up to date - returns FD_BHO_STATE_NOT_CHANGED.
	 * If error occurs in the one of previous steps returns FD_BHO_STATE_ERROR.
	 */
	fd_uint install();

	/*
	 * Tries to uninstall the BHO. Returns FD_BHO_STATE_UNINSTALLED if succeed and FD_BHO_STATE_ERROR
	 * in case error occurred due BHO uninstallation.
	 */
	fd_uint uninstall();

	/*
	 * Returns unmodifiable list of all fd_ieinstance instances registered on current moment.
	 */
	inline const std::vector<fd_ieinstance*> list() {
		return _registered;
	}

	/*
	 * Attaches listener to watch for the instances changes.
	 * listener is the function that takes tree arguments: first one is pointer
	 * to fd_ieinstance object, second is index of instance in the instances vector
	 * and third one is resulting state (either FD_BHO_STATE_ACTIVATED or FD_BHO_STATE_INACTIVATED)
	 */
	inline void listen(void (*fn) (fd_ieinstance*, size_t, fd_uint)) {
		observer->add(fn);
	}

	/*
	 * Removes previously attached listener.
	 * If listener was not attached - does nothing.
	 */
	inline void unlisten(void (*fn) (fd_ieinstance*, size_t, fd_uint)) {
		observer->remove(fn);
	}

	/**
	 * Return name of the debugger.
	 */
	std::string name();
};


#endif /* FD_BRIDGE_H_ */
