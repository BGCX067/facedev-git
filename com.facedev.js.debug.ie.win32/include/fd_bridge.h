/*
 * Provides interface for the bridge between java and internet explorer.
 *
 *  Created on: 07.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef FD_BRIDGE_H_
#define FD_BRIDGE_H_

#include "../../com.facedev.native.common/include/fd_common.h"
#include <vector>
#include <string>
#include <windows.h>
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

	std::vector<ieinstance*> _registered;

	bridge(){
	}

	~bridge();

	static BOOL fd_enum_ie_win(HWND, LPARAM);
	static void register_handle(HWND);
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
	 * Checks if bridge is installed and up to date.
	 * If reset was successful - returns FD_BRIDGE_STATE_NOT_CHANGED.
	 * If error occurs - returns FD_BRIDGE_STATE_ERROR.
	 */
	fd_uint reset();

	/*
	 * Returns unmodifiable list of all fd::ieinstance instances registered on current moment.
	 */
	inline const std::vector<ieinstance*> list() {
		return _registered;
	}

	/**
	 * Return name of the debugger.
	 */
	const std::string name();
};

} // namespace fd


#endif /* FD_BRIDGE_H_ */
