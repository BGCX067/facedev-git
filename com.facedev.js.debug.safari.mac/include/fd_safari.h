/*
 * Safari-specific functions.
 * Bridge between safari browser nad java.
 *
 *  Created on: Mar 27, 2013
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef FD_SAFARI_H_
#define FD_SAFARI_H_

#include "../../com.facedev.native.common/include/fd_common.h"
#include <vector>
#include <string>

#define FD_SAFARI_INSTALLED ((fd_uint)1)

#define FD_SAFARI_UNINSTALLED ((fd_uint)2)

#define FD_SAFARI_NOT_CHANGED ((fd_uint)3)

#define FD_SAFARI_ERROR ((fd_uint)4)

namespace fd {

class safari_bridge;

class safari_instance {
private:
	friend class safari_bridge;

	std::string _name;

	safari_instance(std::string name) {
		_name = name;
	}

	~safari_instance() {}
public:
	inline std::string name() {
		return _name;
	}
};

class safari_bridge {
private:
	static safari_bridge* instance;

	safari_bridge();
	~safari_bridge();
public:
	inline static safari_bridge* get() {
		if (instance == fd_null) {
			instance = new safari_bridge();
		}
		return instance;
	}

	/*
	 * Installs this driver.
	 * Returns FD_SAFARI_INSTALLED in case when installed,
	 * FD_SAFARI_NOT_CHANGED in case when already installed and
	 * FD_SAFARI_ERROR in case of error
	 */
	fd_uint install();

	/*
	 * Uninstalls this driver.
	 * Returns FD_SAFARI_UNINSTALLED in case when uninstalled,
	 * FD_SAFARI_NOT_CHANGED in case when already uninstalled and
	 * FD_SAFARI_ERROR in case of error
	 */
	fd_uint uninstall();

	/*
	 * Returns list of instances of running safari windows.
	 */
	std::vector<safari_instance*> list();

	/*
	 * Returns name of this driver.
	 */
	const char* name();
};

} // namespace fd

#endif /* FD_SAFARI_H_ */
