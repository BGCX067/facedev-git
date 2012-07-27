package com.facedev.js.editor.syntax;

/**
 * Enumeration of all keywords and reserved worlds for the javascript/ecmascript language.
 * This list of reserved words includes several keywords (*) that are reserved for future use 
 * as well as some (**) that are only used in Javascript 2.0 (proposed next Javascript version).
 *  
 * @author alex.bereznevatiy@gmail.com
 *
 */
public enum JsKeyword {
	ABSTRACT, // abstract (*)
	AS, // as (**)
	BOOLEAN, // boolean
	BREAK, // break
	BYTE, // byte
	CASE, // case
	CATCH, // catch
	CHAR, // char
	CLASS, // class (**)
	CONTINUE, // continue
	CONST, // const (**)
	DEBUGGER, // debugger (*)
	DEFAULT, // default
	DELETE, // delete
	DO, // do
	DOUBLE, // double
	ELSE, // else
	ENUM, // enum (*)
	EXPORT, // export (**)
	EXTENDS, // extends (**)
	FALSE, // false
	FINAL, // final
	FINALLY, // finally
	FLOAT, // float
	FOR, // for
	FUNCTION, // function
	GOTO, // goto (*)
	IF, // if
	IMPLEMENTS, // implements (*)
	IMPORT, // import (**)
	IN, // in
	INSTANCEOF, // instanceof
	INT, // int
	INTERFACE, // interface (**)
	IS, // is (**)
	LONG, // long
	NAMESPACE, // namespace (**)
	NATIVE, // native (*)
	NEW, // new
	NULL, // null
	PACKAGE, // package (**)
	PRIVATE, // private (**)
	PROTECTED, // protected (*)
	PUBLIC, // public (**)
	RETURN, // return
	SHORT, // short
	STATIC, // static (**)
	SUPER, // super (**)
	SWITCH, // switch
	SYNCHRONIZED, // synchronized (*)
	THIS, // this
	THROW, // throw
	THROWS, // throws (*)
	TRANSIENT, // transient (*)
	TRUE, // true
	TRY, // try
	TYPEOF, // typeof
	USE, // use (**)
	VAR, // var
	VOID, // void
	VOLATILE, // volatile (*)
	WHILE, // while
	WITH //with
	;

	/**
	 * @return keyword name representing this specific keyword
	 */
	public String getKeyword() {
		return name().toLowerCase();
	}
}
