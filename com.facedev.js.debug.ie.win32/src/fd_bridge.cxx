/*
 * Bridge implementation.
 *
 *  Created on: 07.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#include "../include/fd_bridge.h"
#include "../include/fd_ie.h"

const GUID FACEDEV_IE_BHO_GUID = { 0x82e196fe, 0xef7c, 0x4d76, { 0xb6, 0x4e, 0xc4, 0xea, 0x60, 0x5a, 0x33, 0x56 } };

fd_bridge* fd_bridge::instance = fd_null;

fd_uint fd_bridge::install() {
	_registered.push_back(new fd_ieinstance("First"));
	_registered.push_back(new fd_ieinstance("Second"));
	return FD_BHO_STATE_INSTALLED;
}


fd_uint fd_bridge::uninstall() {
	_registered.clear();
	return FD_BHO_STATE_UNINSTALLED;
}

std::string fd_bridge::name() {
	return "Internet Explorer (test)";
}
