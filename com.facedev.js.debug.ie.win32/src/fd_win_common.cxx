/*
 * Implementation for fd_win_common.h functions.
 *
 *  Created on: 26.03.2013
 *      Author: alex.bereznevatiy@gmail.com
 */

#include <windows.h>
#include <lmerr.h>
#include <tchar.h>
#include "../include/fd_win_common.h"

fd_bool fd_win_check_n_log(HRESULT hresult) {
	if (SUCCEEDED(hresult)) {
		return fd_true;
	}

	// Procedure is taken from http://support.microsoft.com/kb/256348/en-us

	DWORD ret;        // Temp space to hold a return value.
	HINSTANCE hInst;  // Instance handle for DLL.
	HLOCAL pBuffer;   // Buffer to hold the textual error description.

	if ( HRESULT_FACILITY(hresult) == FACILITY_MSMQ ) {
		// MSMQ errors only (see winerror.h for facility info).
		// Load the MSMQ library containing the error message strings.
		hInst = LoadLibrary( TEXT("MQUTIL.DLL") );
		if (hInst != 0) {
			// hInst not NULL if the library was successfully loaded.
			// Get the text string for a message definition
			ret = FormatMessage(
					FORMAT_MESSAGE_ALLOCATE_BUFFER | // Function will handle memory allocation.
					FORMAT_MESSAGE_FROM_HMODULE | // Using a module's message table.
					FORMAT_MESSAGE_IGNORE_INSERTS,
					hInst, // Handle to the DLL.
					hresult, // Message identifier.
					MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), // Default language.
					(LPTSTR)&pBuffer, // Buffer that will hold the text string.
	 				0, NULL);
		}

	} else if ( hresult >= NERR_BASE && hresult <= MAX_NERR ) { // Could be a network error.
		// Load the library containing network messages.
		hInst = LoadLibrary( TEXT("NETMSG.DLL") );
		if (hInst != 0) {
			// Not NULL if successfully loaded.
			// Get a text string for the message definition.
			ret = FormatMessage(
					FORMAT_MESSAGE_ALLOCATE_BUFFER | // The function will allocate memory for the message.
					FORMAT_MESSAGE_FROM_HMODULE | // Message definition is in a module.
					FORMAT_MESSAGE_IGNORE_INSERTS,  // No inserts used.
					hInst, // Handle to the module containing the definition.
					hresult, // Message identifier.
					MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), // Default language.
					(LPTSTR)&pBuffer, // Buffer to hold the text string.
					0, NULL);
		}

	} else {
		// Get the message string from the system.
		ret = FormatMessage(
				FORMAT_MESSAGE_ALLOCATE_BUFFER | // The function will allocate space for pBuffer.
				FORMAT_MESSAGE_FROM_SYSTEM | // System wide message.
				FORMAT_MESSAGE_IGNORE_INSERTS, // No inserts.
				NULL, // Message is not in a module.
				hresult, // Message identifier.
				MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), // Default language.
				(LPTSTR)&pBuffer, // Buffer to hold the text string.
				0, NULL);
	}


	// Display the string.

	if (ret) {
		fd_error("Code: 0x%lX, Message: %s", hresult, (LPTSTR)pBuffer);
	} else {
		fd_error("Unknown error: 0x%lX", hresult);
	}

	LocalFree(pBuffer);

	return fd_false;
}
