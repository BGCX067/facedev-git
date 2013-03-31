/*
 * Safari-specific functions implementation for MacOS.
 *
 *  Created on: Mar 27, 2013
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifdef __APPLE__

#include "../include/fd_safari.h"

fd::safari_bridge* fd::safari_bridge::instance = fd_null;

fd::safari_bridge::safari_bridge() {
}

fd::safari_bridge::~safari_bridge() {
}

fd_uint fd::safari_bridge::install() {
	return FD_SAFARI_INSTALLED;
}

fd_uint fd::safari_bridge::uninstall() {
	return FD_SAFARI_UNINSTALLED;
}

std::vector<fd::safari_instance*> fd::safari_bridge::list() {
	std::vector<fd::safari_instance*> result;

	result.push_back(new fd::safari_instance("First"));
	result.push_back(new fd::safari_instance("Second"));
	result.push_back(new fd::safari_instance("Third"));

	return result;
}

const char* fd::safari_bridge::name() {
	return "Safari (Unknown version)";
}

//CGWindowListCopyWindowInfo

#endif //#ifdef __APPLE__
