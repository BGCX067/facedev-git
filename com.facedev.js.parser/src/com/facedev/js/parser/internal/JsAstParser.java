package com.facedev.js.parser.internal;

import java.io.IOException;
import java.util.List;

import com.facedev.js.parser.JsKeywords;
import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.JsPunctuators;

/**
 * Top-down implementation of javascript parser. Builds abstract syntax tree (AST) from stream of tokens. 
 * 
 * Note that this class violates java naming convention (method should start with lowercase) bacause it uses
 * own convention to follow naming from the ECMA-262 lexical specification
 * 
 * @author alex.bereznevatiy@gmail.com
 */
class JsAstParser implements JsKeywords, JsPunctuators {
	
	private JsTokensBuffer buffer;
	private JsParseLogger logger;

	JsAstParser(JsTokenizer tokenizer, JsParseLogger logger) throws IOException, JsParseException {
		this.buffer = new JsTokensBuffer(tokenizer);
		this.logger = logger;
	}

	/**
	 * @return list of global nodes of AST.
	 * @throws JsParseException 
	 * @throws IOException 
	 */
	List<JsSyntaxNode> parse() throws IOException, JsParseException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/***********************************************************************************************************
	 * 
	 *                                     STATEMENT EXPRESSIONS SECTION
	 * 
	 ***********************************************************************************************************/

	private boolean FunctionExpression() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean FunctionBody() {
		// TODO Auto-generated method stub
		return false;
	}

	/***********************************************************************************************************
	 * 
	 *                                       SIMPLE EXPRESSIONS SECTION
	 * 
	 ***********************************************************************************************************/
	
	private boolean Expression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && AssignmentExpression() && buffer.isPunktuator(COMA) && Expression() ) || (
			save.rollback() && AssignmentExpression()
		);
	}

	private boolean ExpressionNoIn() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && AssignmentExpressionNoIn() && buffer.isPunktuator(COMA) && ExpressionNoIn() ) || (
			save.rollback() && AssignmentExpressionNoIn()
		);
	}
	
	private boolean PrimaryExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
 			save.rollback() && buffer.isKeyword(KEYWORD_THIS)              ) || (
			save.rollback() && buffer.isIdentifier()                     ) || (
			save.rollback() && buffer.isLiteral()                        ) || (
			save.rollback() && ArrayLiteral()                            ) || (
			save.rollback() && ObjectLiteral()                           ) || (
			save.rollback() && buffer.isPunktuator(OPEN_BRACKET) && 
					Expression() && buffer.isPunktuator(CLOSE_BRACKET)                       
		);
				
	}

	private boolean ObjectLiteral() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isPunktuator(OPEN_CURVY_BRACKET) && 
					buffer.isPunktuator(CLOSE_CURVY_BRACKET) ) || (
			save.rollback() && buffer.isPunktuator(OPEN_CURVY_BRACKET) && 
					PropertyNameAndValueList() && buffer.isPunktuator(CLOSE_CURVY_BRACKET) ) || (
			save.rollback() && buffer.isPunktuator(OPEN_CURVY_BRACKET) && 
					PropertyNameAndValueList() && buffer.isPunktuator(COMA) && 
					buffer.isPunktuator(CLOSE_CURVY_BRACKET)
		);
	}

	private boolean PropertyNameAndValueList() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && PropertyAssignment() && buffer.isPunktuator(COMA) && 
					PropertyNameAndValueList() ) || (
			save.rollback() && PropertyAssignment()
		);
	}

	private boolean PropertyAssignment() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && PropertyName() && buffer.isPunktuator(COLON) && 
					AssignmentExpression() ) || ( 
			save.rollback() && buffer.isIdentifier("get") && PropertyName() &&
					buffer.isPunktuator(OPEN_BRACKET) && buffer.isPunktuator(CLOSE_BRACKET) &&
					buffer.isPunktuator(OPEN_CURVY_BRACKET) && FunctionBody() && 
					buffer.isPunktuator(CLOSE_CURVY_BRACKET) ) || (
			save.rollback() && buffer.isIdentifier("set") && PropertyName() &&
					buffer.isPunktuator(OPEN_BRACKET) && buffer.isIdentifier() && 
					buffer.isPunktuator(CLOSE_BRACKET) && buffer.isPunktuator(OPEN_CURVY_BRACKET) &&  
					FunctionBody() && buffer.isPunktuator(CLOSE_CURVY_BRACKET)  
					
		);
	}

	private boolean PropertyName() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isIdentifier() ) || (
			save.rollback() && buffer.isStringLiteral() ) || (
			save.rollback() && buffer.isNumericLiteral()
		);
	}

	private boolean ArrayLiteral() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isPunktuator(OPEN_SQUARE_BRACKET) && Ellision() && 
					buffer.isPunktuator(CLOSE_SQUARE_BRACKET) ) || (
			save.rollback() && buffer.isPunktuator(OPEN_SQUARE_BRACKET) && ElementsList() && 
					buffer.isPunktuator(CLOSE_SQUARE_BRACKET) ) || (
			save.rollback() && buffer.isPunktuator(OPEN_SQUARE_BRACKET) && ElementsList() && 
					buffer.isPunktuator(COMA) && Ellision() && buffer.isPunktuator(CLOSE_SQUARE_BRACKET) 
			);
	}

	private boolean ElementsList() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && Ellision() && AssignmentExpression() && 
					buffer.isPunktuator(COMA) && ElementsList()) || (
			save.rollback() && Ellision() && AssignmentExpression()
			);
	}
	
	private boolean Ellision() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		while (buffer.isPunktuator(COMA)) {
			save.forward();
		}
		save.rollback();
		return true;
	}
	
	private boolean MemberExpression() throws IOException, JsParseException {
		if (!SubMemberExpression()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		while ((buffer.isPunktuator(DOT) && buffer.isIdentifier()) 
					|| (
				buffer.isPunktuator(OPEN_SQUARE_BRACKET) && Expression() && 
						buffer.isPunktuator(CLOSE_SQUARE_BRACKET))
			) {
			save = buffer.createSavePoint();
		}
		save.rollback();
		return true;
	}
	
	private boolean SubMemberExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isKeyword(KEYWORD_NEW) && MemberExpression() && Arguments()) || (
			save.rollback() && FunctionExpression() ) || (
			save.rollback() && PrimaryExpression()
		);
	}
	
	private boolean NewExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isKeyword(KEYWORD_NEW) && NewExpression()) || (
			save.rollback() && MemberExpression()
		);
	}
	
	private boolean CallExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		if (!MemberExpression()) {
			return false;
		}
		
		while (MemberExpression()) {
			save = buffer.createSavePoint();
		}
		
		boolean result = false;
		
		while ((save.rollback() && Arguments()) || 
				(save.rollback() && buffer.isPunktuator(DOT) && buffer.isIdentifier()) ||
				(save.rollback() && buffer.isPunktuator(OPEN_SQUARE_BRACKET) && 
						Expression() && buffer.isPunktuator(CLOSE_SQUARE_BRACKET))) {
			result = true;
			save = buffer.createSavePoint();
		}
		return result;	
	}

	private boolean Arguments() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isPunktuator(OPEN_BRACKET) && buffer.isPunktuator(CLOSE_BRACKET)) || (
			save.rollback() && buffer.isPunktuator(OPEN_BRACKET) && ArgumentList() && buffer.isPunktuator(CLOSE_BRACKET)
		);
	}

	private boolean ArgumentList() throws IOException, JsParseException {
		if (!AssignmentExpression()) {
			return false;
		}

		SavePoint save = buffer.createSavePoint();
		
		while (buffer.isPunktuator(COMA) && AssignmentExpression()) {
			save = buffer.createSavePoint();
		}
		return save.rollback();
	}
	
	private boolean LeftHandSideExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		
		return (
			save.rollback() && NewExpression()) || (
			save.rollback() && CallExpression()
		);
	}
	
	private boolean PostfixExpression() throws IOException, JsParseException {
		if (!LeftHandSideExpression()) {
			return false;
		}
		if (buffer.isTerminated()) {
			return true;
		}
		
		SavePoint save = buffer.createSavePoint();
		
		if (buffer.isPunktuator(PLUSPLUS)) {
			return true;
		}
		
		save.rollback();
		
		if (buffer.isPunktuator(MINUSMINUS)) {
			return true;
		}
		
		return save.rollback();
	}
	
	private boolean UnaryExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		
		return (
			save.rollback() && buffer.isKeyword(JsKeywords.KEYWORD_DELETE) && UnaryExpression() ) || (
			save.rollback() && buffer.isKeyword(JsKeywords.KEYWORD_VOID) && UnaryExpression() ) || (
			save.rollback() && buffer.isKeyword(JsKeywords.KEYWORD_TYPEOF) && UnaryExpression() ) || (
			save.rollback() && buffer.isPunktuator(PLUSPLUS) && UnaryExpression() ) || (
			save.rollback() && buffer.isPunktuator(MINUSMINUS) && UnaryExpression() ) || (
			save.rollback() && buffer.isPunktuator(PLUS) && UnaryExpression() ) || (
			save.rollback() && buffer.isPunktuator(MINUS) && UnaryExpression() ) || (
			save.rollback() && buffer.isPunktuator(TILDA) && UnaryExpression() ) || (
			save.rollback() && buffer.isPunktuator(EXCLAMATION) && UnaryExpression() ) || (
			save.rollback() && PostfixExpression()
			);
	}
	
	private boolean MultiplicativeExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && UnaryExpression() && buffer.isPunktuator(MULTIPLICATION) && MultiplicativeExpression() ) || (
			save.rollback() && UnaryExpression() && buffer.isPunktuator(DIVISION) && MultiplicativeExpression() ) || (
			save.rollback() && UnaryExpression() && buffer.isPunktuator(MODULO) && MultiplicativeExpression() ) || (
			save.rollback() && UnaryExpression()
		);
	}
	
	private boolean AdditiveExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		
		return (
			save.rollback() && MultiplicativeExpression() && buffer.isPunktuator(PLUS) && AdditiveExpression() ) || (
			save.rollback() && MultiplicativeExpression() && buffer.isPunktuator(MINUS) && AdditiveExpression() ) || (
			save.rollback() && MultiplicativeExpression()
		);
	}
	
	private boolean ShiftExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		
		return (
			save.rollback() && AdditiveExpression() && buffer.isPunktuator(SHIFT_LEFT) && ShiftExpression() ) || (
			save.rollback() && AdditiveExpression() && buffer.isPunktuator(SHIFT_RIGHT) && ShiftExpression() ) || (
			save.rollback() && AdditiveExpression() && buffer.isPunktuator(SHIFT_RIGHT_UNSIGNED) && ShiftExpression() ) || (
			save.rollback() && AdditiveExpression()
		);
	}
	
	private boolean RelationalExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		
		return (
			save.rollback() && ShiftExpression() && buffer.isPunktuator(LESS) && RelationalExpression() ) || (
			save.rollback() && ShiftExpression() && buffer.isPunktuator(GREATER) && RelationalExpression() ) || (
			save.rollback() && ShiftExpression() && buffer.isPunktuator(LESS_OR_EQUAL) && RelationalExpression() ) || (
			save.rollback() && ShiftExpression() && buffer.isPunktuator(GREATER_OR_EQUAL) && RelationalExpression() ) || (
			save.rollback() && ShiftExpression() && buffer.isKeyword(JsKeywords.KEYWORD_INSTANCEOF) && RelationalExpression() ) || (
			save.rollback() && ShiftExpression() && buffer.isKeyword(JsKeywords.KEYWORD_IN) && RelationalExpression() ) || (
			save.rollback() && ShiftExpression()
		);
	}
	
	private boolean RelationalExpressionNoIn() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		
		return (
			save.rollback() && ShiftExpression() && buffer.isPunktuator(LESS) && RelationalExpressionNoIn() ) || (
			save.rollback() && ShiftExpression() && buffer.isPunktuator(GREATER) && RelationalExpressionNoIn() ) || (
			save.rollback() && ShiftExpression() && buffer.isPunktuator(LESS_OR_EQUAL) && RelationalExpressionNoIn() ) || (
			save.rollback() && ShiftExpression() && buffer.isPunktuator(GREATER_OR_EQUAL) && RelationalExpressionNoIn() ) || (
			save.rollback() && ShiftExpression() && buffer.isKeyword(JsKeywords.KEYWORD_INSTANCEOF) && RelationalExpressionNoIn() ) || (
			save.rollback() && ShiftExpression()
		);
	}
	
	private boolean EqualityExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		
		return (
			save.rollback() && RelationalExpression() && buffer.isPunktuator(EQUALS) && EqualityExpression() ) || (
			save.rollback() && RelationalExpression() && buffer.isPunktuator(NOT_EQUALS) && EqualityExpression() ) || (
			save.rollback() && RelationalExpression() && buffer.isPunktuator(STRICT_EQUALS) && EqualityExpression() ) || (
			save.rollback() && RelationalExpression() && buffer.isPunktuator(STRICT_NOT_EQUALS) && EqualityExpression() ) || (
			save.rollback() && RelationalExpression()
		);
	}
	
	private boolean EqualityExpressionNoIn() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		
		return (
			save.rollback() && RelationalExpressionNoIn() && buffer.isPunktuator(EQUALS) && EqualityExpressionNoIn() ) || (
			save.rollback() && RelationalExpressionNoIn() && buffer.isPunktuator(NOT_EQUALS) && EqualityExpressionNoIn() ) || (
			save.rollback() && RelationalExpressionNoIn() && buffer.isPunktuator(STRICT_EQUALS) && EqualityExpressionNoIn() ) || (
			save.rollback() && RelationalExpressionNoIn() && buffer.isPunktuator(STRICT_NOT_EQUALS) && EqualityExpressionNoIn() ) || (
			save.rollback() && RelationalExpressionNoIn()
		);
	}
	
	private boolean BitwiseANDExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && EqualityExpression() && buffer.isPunktuator(AND) && BitwiseANDExpression() ) || (
			save.rollback() && EqualityExpression()
		);
	}
	
	private boolean BitwiseANDExpressionNoIn() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && EqualityExpressionNoIn() && buffer.isPunktuator(AND) && BitwiseANDExpressionNoIn() ) || (
			save.rollback() && EqualityExpressionNoIn()
		);
	}
	
	private boolean BitwiseXORExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && BitwiseANDExpression() && buffer.isPunktuator(XOR) && BitwiseXORExpression() ) || (
			save.rollback() && BitwiseANDExpression()
		);
	}
	
	private boolean BitwiseXORExpressionNoIn() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && BitwiseANDExpressionNoIn() && buffer.isPunktuator(XOR) && BitwiseXORExpressionNoIn() ) || (
			save.rollback() && BitwiseANDExpressionNoIn()
		);
	}
	
	private boolean BitwiseORExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && BitwiseXORExpression() && buffer.isPunktuator(OR) && BitwiseORExpression() ) || (
			save.rollback() && BitwiseXORExpression()
		);
	}
	
	private boolean BitwiseORExpressionNoIn() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && BitwiseXORExpressionNoIn() && buffer.isPunktuator(OR) && BitwiseORExpressionNoIn() ) || (
			save.rollback() && BitwiseXORExpressionNoIn()
		);
	}
	
	private boolean LogicalANDExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && BitwiseORExpression() && buffer.isPunktuator(ANDAND) && LogicalANDExpression() ) || (
			save.rollback() && BitwiseORExpression()
		);
	}
	
	private boolean LogicalANDExpressionNoIn() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && BitwiseORExpressionNoIn() && buffer.isPunktuator(ANDAND) && LogicalANDExpressionNoIn() ) || (
			save.rollback() && BitwiseORExpressionNoIn()
		);
	}
	
	private boolean LogicalORExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && LogicalANDExpression() && buffer.isPunktuator(OROR) && LogicalORExpression() ) || (
			save.rollback() && LogicalANDExpression()
		);
	}
	
	private boolean LogicalORExpressionNoIn() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && LogicalANDExpressionNoIn() && buffer.isPunktuator(OROR) && LogicalORExpressionNoIn() ) || (
			save.rollback() && LogicalANDExpressionNoIn()
		);
	}
	
	private boolean ConditionalExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && LogicalORExpression() && buffer.isPunktuator(QUESTION) && AssignmentExpression() && 
					buffer.isPunktuator(COLON) && AssignmentExpression() ) || (
			save.rollback() && LogicalORExpression()
		);
	}
	
	private boolean ConditionalExpressionNoIn() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && LogicalORExpressionNoIn() && buffer.isPunktuator(QUESTION) && AssignmentExpression() && 
					buffer.isPunktuator(COLON) && AssignmentExpressionNoIn() ) || (
			save.rollback() && LogicalORExpressionNoIn()
		);
	}

	private boolean AssignmentExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && ConditionalExpression() ) || (
			save.rollback() && LeftHandSideExpression() && buffer.isPunktuator(ASSIGNMENT) && AssignmentExpression() ) || (
			save.rollback() && LeftHandSideExpression() && AssignmentOperator() && AssignmentExpression()
		);
	}

	private boolean AssignmentExpressionNoIn() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && ConditionalExpressionNoIn() ) || (
			save.rollback() && LeftHandSideExpression() && buffer.isPunktuator(ASSIGNMENT) && AssignmentExpressionNoIn() ) || (
			save.rollback() && LeftHandSideExpression() && AssignmentOperator() && AssignmentExpressionNoIn()
		);
	}

	private boolean AssignmentOperator() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isPunktuator(ASSIGNMENT_MUL) ) || (
			save.rollback() && buffer.isPunktuator(ASSIGNMENT_DIV) ) || (
			save.rollback() && buffer.isPunktuator(ASSIGNMENT_MOD) ) || (
			save.rollback() && buffer.isPunktuator(ASSIGNMENT_SUM) ) || (
			save.rollback() && buffer.isPunktuator(ASSIGNMENT_SUB) ) || (
			save.rollback() && buffer.isPunktuator(ASSIGNMENT_SHL) ) || (
			save.rollback() && buffer.isPunktuator(ASSIGNMENT_SHR) ) || (
			save.rollback() && buffer.isPunktuator(ASSIGNMENT_USHR) ) || (
			save.rollback() && buffer.isPunktuator(ASSIGNMENT_AND) ) || (
			save.rollback() && buffer.isPunktuator(ASSIGNMENT_XOR) ) || (
			save.rollback() && buffer.isPunktuator(ASSIGNMENT_OR)
		);
	}

}
