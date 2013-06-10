package com.facedev.js.parser.internal;

import java.io.IOException;
import java.util.List;

import com.facedev.js.parser.JsKeywords;
import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.JsParseLogger.Message;
import com.facedev.js.parser.JsPunctuators;
import com.facedev.js.parser.Token;

/**
 * Top-down implementation of javascript parser. Builds abstract syntax tree (AST) from stream of tokens. 
 * 
 * Note that this class violates java naming convention (method should start with lowercase) bacause it uses
 * own convention to follow naming from the ECMA-262 lexical specification
 * 
 * @author alex.bereznevatiy@gmail.com
 */
class JsAstParser implements JsKeywords, JsPunctuators {
	
	private final JsTokensBuffer buffer;
	private final JsParseLogger logger;
	private final JsAstBuilder builder;

	JsAstParser(JsTokenizer tokenizer, JsParseLogger logger) throws IOException, JsParseException {
		this.buffer = new JsTokensBuffer(tokenizer);
		this.logger = logger;
		this.builder = new JsAstBuilder();
	}

	/**
	 * @return list of global nodes of AST or <code>null</code> if parsing fails.
	 * @throws JsParseException 
	 * @throws IOException 
	 */
	List<JsSyntaxNode> parse() throws IOException, JsParseException {
		if (!Program()) {
			return null;
		}
		return builder.create();
	}
	
	/***********************************************************************************************************
	 * 
	 *                                     PROGRAM EXPRESSIONS SECTION
	 * 
	 ***********************************************************************************************************/

	private boolean FunctionDeclaration() throws IOException, JsParseException {
		return buffer.isKeyword(KEYWORD_FUNCTION) && buffer.isIdentifier() && buffer.isPunktuator(OPEN_BRACKET) &&
				FormalParameterListOpt() && buffer.isPunktuator(CLOSE_BRACKET) && buffer.isPunktuator(OPEN_CURVY_BRACKET) &&
				FunctionBody() && buffer.isPunktuator(CLOSE_CURVY_BRACKET);
	}
	
	private boolean FormalParameterListOpt() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		while (buffer.isIdentifier()) {
			save.forward();
			if (!buffer.isPunktuator(COMA)) {
				return save.rollback();
			}
			save.forward();
		}
		return save.rollback();
	}

	private boolean FunctionExpression() throws IOException, JsParseException {
		if (!buffer.isKeyword(KEYWORD_FUNCTION)) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		if (!buffer.isIdentifier()) {
			save.rollback();
		}
		
		return buffer.isPunktuator(OPEN_BRACKET) &&	FormalParameterListOpt() && buffer.isPunktuator(CLOSE_BRACKET) && 
				buffer.isPunktuator(OPEN_CURVY_BRACKET) && FunctionBody() && buffer.isPunktuator(CLOSE_CURVY_BRACKET);
	}

	private boolean FunctionBody() throws IOException, JsParseException {
		return SourceElementsOpt();
	}
	
	private boolean Program() throws IOException, JsParseException {
		return SourceElementsOpt() && !buffer.hasNext();
	}
	
	private boolean SourceElementsOpt() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		while (SourceElement()) {
			save = buffer.createSavePoint();
		}
		
		return save.rollback();
	}

	private boolean SourceElement() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && Statement() ) || (
			save.rollback() && FunctionDeclaration()
		);
	}

	/***********************************************************************************************************
	 * 
	 *                                     STATEMENT EXPRESSIONS SECTION
	 * 
	 ***********************************************************************************************************/
	
	private boolean Statement() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && Block() ) || (
			save.rollback() && VariableStatement() ) || (
			save.rollback() && EmptyStatement() ) || (
			save.rollback() && ExpressionStatement() ) || (
			save.rollback() && IfStatement() ) || (
			save.rollback() && IterationStatement() ) || (
			save.rollback() && ContinueStatement() ) || (
			save.rollback() && BreakStatement() ) || (
			save.rollback() && ReturnStatement() ) || (
			save.rollback() && WithStatement() ) || (
			save.rollback() && LabelledStatement() ) || (
			save.rollback() && SwitchStatement() ) || (
			save.rollback() && ThrowStatement() ) || (
			save.rollback() && TryStatement() ) || (
			save.rollback() && DebuggerStatement()
		);
	}

	private boolean Block() throws IOException, JsParseException {
		if (!buffer.isPunktuator(OPEN_CURVY_BRACKET)) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		if (buffer.isPunktuator(CLOSE_CURVY_BRACKET)) {
			return true;
		}
		save.rollback();
		return StatementList() && buffer.isPunktuator(CLOSE_CURVY_BRACKET);
	}

	private boolean StatementList() throws IOException, JsParseException {
		if (!Statement()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		while (Statement()) {
			save = buffer.createSavePoint();
		}
		return save.rollback();
	}

	private boolean VariableStatement() throws IOException, JsParseException {
		if (!buffer.isKeyword(KEYWORD_VAR)) {
			return false;
		}
		return VariableDeclarationList() && 
				buffer.isTerminated() || buffer.isPunktuator(SEMICOLON);
	}

	private boolean VariableDeclarationList() throws IOException, JsParseException {
		if (!VariableDeclaration()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		while (buffer.isPunktuator(COMA) && VariableDeclaration()) {
			save = buffer.createSavePoint();
		}
		return save.rollback();
	}

	private boolean VariableDeclarationListNoIn() throws IOException, JsParseException {
		if (!VariableDeclarationNoIn()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		while (buffer.isPunktuator(COMA) && VariableDeclarationNoIn()) {
			save = buffer.createSavePoint();
		}
		return save.rollback();
	}

	private boolean VariableDeclaration() throws IOException, JsParseException {
		if (!buffer.isIdentifier()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		if (!Initializer()) {
			save.rollback();
		}
		return true;
	}

	private boolean VariableDeclarationNoIn() throws IOException, JsParseException {
		if (!buffer.isIdentifier()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		if (!InitializerNoIn()) {
			save.rollback();
		}
		return true;
	}

	private boolean Initializer() throws IOException, JsParseException {
		if (!buffer.isPunktuator(ASSIGNMENT)) {
			return false;
		}
		if (!AssignmentExpression()) {
			logger.log(Message.SYNTAX_ERROR, buffer.getLastReturned());
			return false;
		}
		return true;
	}

	private boolean InitializerNoIn() throws IOException, JsParseException {
		return buffer.isPunktuator(ASSIGNMENT) && AssignmentExpressionNoIn();
	}

	private boolean EmptyStatement() throws IOException, JsParseException {
		if (buffer.isPunktuator(SEMICOLON)) {
			logger.log(Message.STATEMENT_HAS_NO_EFFECT, buffer.getLastReturned());
			return true;
		}
		return false;
	}

	private boolean ExpressionStatement() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		Token next = buffer.next();
		if (next == null || next.isSame(OPEN_CURVY_BRACKET) || next.isSame(KEYWORD_FUNCTION)) {
			return false;
		}
		save.rollback();
		return Expression();
	}

	private boolean IfStatement() throws IOException, JsParseException {
		if (!(buffer.isKeyword(KEYWORD_IF) && buffer.isPunktuator(OPEN_BRACKET) && Expression() &&
				buffer.isPunktuator(CLOSE_BRACKET) && Statement())) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		if (buffer.isKeyword(KEYWORD_ELSE) && Statement()) {
			return true;
		}
		return save.rollback();
	}

	private boolean IterationStatement() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isKeyword(KEYWORD_DO) && Statement() && buffer.isKeyword(KEYWORD_WHILE) && 
					buffer.isPunktuator(OPEN_BRACKET) && Expression() && buffer.isPunktuator(CLOSE_BRACKET) && 
					(buffer.isTerminated() || buffer.isPunktuator(SEMICOLON)) ) || (
			save.rollback() && buffer.isKeyword(KEYWORD_WHILE) && buffer.isPunktuator(OPEN_BRACKET) && Expression() && 
					buffer.isPunktuator(CLOSE_BRACKET) && Statement() ) || (
			save.rollback() && buffer.isKeyword(KEYWORD_FOR) && buffer.isPunktuator(OPEN_BRACKET) &&
					ExpressionNoInOpt() && buffer.isPunktuator(SEMICOLON) && ExpressionOpt() && buffer.isPunktuator(SEMICOLON) &&
					ExpressionOpt() && buffer.isPunktuator(CLOSE_BRACKET) && Statement() ) || (
			save.rollback() && buffer.isKeyword(KEYWORD_FOR) && buffer.isPunktuator(OPEN_BRACKET) &&
					buffer.isKeyword(KEYWORD_VAR) && VariableDeclarationListNoIn() && buffer.isPunktuator(SEMICOLON) && 
					ExpressionOpt() && buffer.isPunktuator(SEMICOLON) &&
					ExpressionOpt() && buffer.isPunktuator(CLOSE_BRACKET) && Statement() ) || (
			save.rollback() && buffer.isKeyword(KEYWORD_FOR) && buffer.isPunktuator(OPEN_BRACKET) &&
					LeftHandSideExpression() && buffer.isKeyword(KEYWORD_IN) && Expression() && 
					buffer.isPunktuator(CLOSE_BRACKET) && Statement() ) || (
			save.rollback() && buffer.isKeyword(KEYWORD_FOR) && buffer.isPunktuator(OPEN_BRACKET) &&
					buffer.isKeyword(KEYWORD_VAR) && VariableDeclarationListNoIn() && buffer.isKeyword(KEYWORD_IN) && 
					Expression() &&	buffer.isPunktuator(CLOSE_BRACKET) && Statement()
		);
	}

	private boolean ExpressionNoInOpt() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		if (!ExpressionNoIn()) {
			save.rollback();
		}
		return true;
	}
	
	private boolean ExpressionOpt() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		if (!Expression()) {
			save.rollback();
		}
		return true;
	}

	private boolean ContinueStatement() throws IOException, JsParseException {
		if (!buffer.isKeyword(KEYWORD_CONTINUE)) {
			return false;
		}
		if (buffer.isTerminated()) return true;
		SavePoint save = buffer.createSavePoint();
		if (!buffer.isIdentifier()) {
			save.rollback();
		}
		return buffer.isTerminated() || buffer.isPunktuator(SEMICOLON);
	}

	private boolean BreakStatement() throws IOException, JsParseException {
		if (!buffer.isKeyword(KEYWORD_BREAK)) {
			return false;
		}
		if (buffer.isTerminated()) return true;
		SavePoint save = buffer.createSavePoint();
		if (!buffer.isIdentifier()) {
			save.rollback();
		}
		return buffer.isTerminated() || buffer.isPunktuator(SEMICOLON);
	}

	private boolean ReturnStatement() throws IOException, JsParseException {
		if (!buffer.isKeyword(KEYWORD_RETURN)) {
			return false;
		}
		if (buffer.isTerminated()) return true;
		SavePoint save = buffer.createSavePoint();
		if (!Expression()) {
			save.rollback();
		}
		return buffer.isTerminated() || buffer.isPunktuator(SEMICOLON);
	}

	private boolean WithStatement() throws IOException, JsParseException {
		if (!buffer.isKeyword(KEYWORD_WITH)) {
			return false;
		}
		logger.log(Message.WITH_STATEMENT, buffer.getLastReturned());
		return buffer.isPunktuator(OPEN_BRACKET) && Expression() && 
				buffer.isPunktuator(CLOSE_BRACKET) && Statement();
	}

	private boolean LabelledStatement() throws IOException, JsParseException {
		return buffer.isIdentifier() && buffer.isPunktuator(COLON) && Statement();
	}

	private boolean SwitchStatement() throws IOException, JsParseException {
		if (!buffer.isKeyword(KEYWORD_SWITCH)) {
			return false;
		}
		return buffer.isPunktuator(OPEN_BRACKET) && Expression() && 
				buffer.isPunktuator(CLOSE_BRACKET) && CaseBlock();
	}

	private boolean CaseBlock() throws IOException, JsParseException {
		if (!buffer.isPunktuator(OPEN_CURVY_BRACKET) || !CaseClausesOpt()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		if (!DefaultClause()) {
			save.rollback();
			return buffer.isPunktuator(CLOSE_CURVY_BRACKET);
		}
		if (!CaseClausesOpt()) {
			return false;
		}
		return buffer.isPunktuator(CLOSE_CURVY_BRACKET);
	}

	private boolean CaseClausesOpt() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		while(CaseClause()) {
			save = buffer.createSavePoint();
		}
		return save.rollback();
	}

	private boolean CaseClause() throws IOException, JsParseException {
		if (!(buffer.isKeyword(KEYWORD_CASE) && Expression() && buffer.isPunktuator(COLON))) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		if (!StatementList()) {
			save.rollback();
		}
		return true;
	}

	private boolean DefaultClause() throws IOException, JsParseException {
		if (!(buffer.isKeyword(KEYWORD_DEFAULT) && buffer.isPunktuator(COLON))) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		if (!StatementList()) {
			save.rollback();
		}
		return true;
	}

	private boolean ThrowStatement() throws IOException, JsParseException {
		if (!buffer.isKeyword(KEYWORD_THROW)) {
			return false;
		}
		if (buffer.isTerminated()) {
			logger.log(Message.SYNTAX_ERROR, buffer.getLastReturned());
			return false;
		}
		return Expression() && (buffer.isTerminated() || buffer.isPunktuator(SEMICOLON));
	}

	private boolean TryStatement() throws IOException, JsParseException {
		if (!buffer.isKeyword(KEYWORD_TRY) && Block()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		boolean catchExists = true;
		if (!Catch()) {
			catchExists = false;
			save.rollback();
		}
		return Finally() || catchExists;
	}

	private boolean Catch() throws IOException, JsParseException {
		return buffer.isKeyword(KEYWORD_CATCH) && buffer.isPunktuator(OPEN_BRACKET) && 
				buffer.isIdentifier() && buffer.isPunktuator(CLOSE_BRACKET) && Block();
	}

	private boolean Finally() throws IOException, JsParseException {
		return buffer.isKeyword(KEYWORD_FINALLY) && Block();
	}

	private boolean DebuggerStatement() throws IOException, JsParseException {
		if (!buffer.isKeyword(KEYWORD_DEBUGGER)) {
			return false;
		}
		return buffer.isTerminated() || buffer.isPunktuator(SEMICOLON);
	}

	/***********************************************************************************************************
	 * 
	 *                                       SIMPLE EXPRESSIONS SECTION
	 * 
	 ***********************************************************************************************************/
	
	private boolean Expression() throws IOException, JsParseException {
		if (!AssignmentExpression()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		if (!buffer.isPunktuator(COMA) || !Expression()) {
			save.rollback();
		}
		return true;
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
		
		while ((buffer.isPunktuator(DOT) && buffer.isIdentifier()) ||
				(save.rollback() && buffer.isPunktuator(OPEN_SQUARE_BRACKET) && 
							Expression() && buffer.isPunktuator(CLOSE_SQUARE_BRACKET)) ||
				(save.rollback() && Arguments())
			) {
			save = buffer.createSavePoint();
		}
		return save.rollback();
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
		if (!buffer.isPunktuator(OPEN_BRACKET)) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isPunktuator(CLOSE_BRACKET)) || (
			save.rollback() && ArgumentList() && buffer.isPunktuator(CLOSE_BRACKET)
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
		if (!UnaryExpression()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isPunktuator(MULTIPLICATION) && MultiplicativeExpression() ) || (
			save.rollback() && buffer.isPunktuator(DIVISION) && MultiplicativeExpression() ) || (
			save.rollback() && buffer.isPunktuator(MODULO) && MultiplicativeExpression() ) || (
			save.rollback() 
		);
	}
	
	private boolean AdditiveExpression() throws IOException, JsParseException {
		
		if (!MultiplicativeExpression()) {
			return false;
		}
		
		SavePoint save = buffer.createSavePoint();
		
		return (
			save.rollback() && buffer.isPunktuator(PLUS) && AdditiveExpression() ) || (
			save.rollback() && buffer.isPunktuator(MINUS) && AdditiveExpression() ) || (
			save.rollback()
		);
	}
	
	private boolean ShiftExpression() throws IOException, JsParseException {
		
		if (!AdditiveExpression()) {
			return false;
		}
		
		SavePoint save = buffer.createSavePoint();
		
		return (
			save.rollback() && buffer.isPunktuator(SHIFT_LEFT) && ShiftExpression() ) || (
			save.rollback() && buffer.isPunktuator(SHIFT_RIGHT) && ShiftExpression() ) || (
			save.rollback() && buffer.isPunktuator(SHIFT_RIGHT_UNSIGNED) && ShiftExpression() ) || (
			save.rollback()
		);
	}
	
	private boolean RelationalExpression() throws IOException, JsParseException {
		
		if (!ShiftExpression()) {
			return false;
		}
		
		SavePoint save = buffer.createSavePoint();
		
		return (
			save.rollback() && buffer.isPunktuator(LESS) && RelationalExpression() ) || (
			save.rollback() && buffer.isPunktuator(GREATER) && RelationalExpression() ) || (
			save.rollback() && buffer.isPunktuator(LESS_OR_EQUAL) && RelationalExpression() ) || (
			save.rollback() && buffer.isPunktuator(GREATER_OR_EQUAL) && RelationalExpression() ) || (
			save.rollback() && buffer.isKeyword(JsKeywords.KEYWORD_INSTANCEOF) && RelationalExpression() ) || (
			save.rollback() && buffer.isKeyword(JsKeywords.KEYWORD_IN) && RelationalExpression() ) || (
			save.rollback()
		);
	}
	
	private boolean RelationalExpressionNoIn() throws IOException, JsParseException {
		
		if (!ShiftExpression()) {
			return false;
		}
		
		SavePoint save = buffer.createSavePoint();
		
		return (
			save.rollback() && buffer.isPunktuator(LESS) && RelationalExpressionNoIn() ) || (
			save.rollback() && buffer.isPunktuator(GREATER) && RelationalExpressionNoIn() ) || (
			save.rollback() && buffer.isPunktuator(LESS_OR_EQUAL) && RelationalExpressionNoIn() ) || (
			save.rollback() && buffer.isPunktuator(GREATER_OR_EQUAL) && RelationalExpressionNoIn() ) || (
			save.rollback() && buffer.isKeyword(JsKeywords.KEYWORD_INSTANCEOF) && RelationalExpressionNoIn() ) || (
			save.rollback() 
		);
	}
	
	private boolean EqualityExpression() throws IOException, JsParseException {
		if (!RelationalExpression()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		
		return (
			save.rollback() && buffer.isPunktuator(EQUALS) && EqualityExpression() ) || (
			save.rollback() && buffer.isPunktuator(NOT_EQUALS) && EqualityExpression() ) || (
			save.rollback() && buffer.isPunktuator(STRICT_EQUALS) && EqualityExpression() ) || (
			save.rollback() && buffer.isPunktuator(STRICT_NOT_EQUALS) && EqualityExpression() ) || (
			save.rollback()
		);
	}
	
	private boolean EqualityExpressionNoIn() throws IOException, JsParseException {
		if (!RelationalExpressionNoIn()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		
		return (
			save.rollback() && buffer.isPunktuator(EQUALS) && EqualityExpressionNoIn() ) || (
			save.rollback() && buffer.isPunktuator(NOT_EQUALS) && EqualityExpressionNoIn() ) || (
			save.rollback() && buffer.isPunktuator(STRICT_EQUALS) && EqualityExpressionNoIn() ) || (
			save.rollback() && buffer.isPunktuator(STRICT_NOT_EQUALS) && EqualityExpressionNoIn() ) || (
			save.rollback()
		);
	}
	
	private boolean BitwiseANDExpression() throws IOException, JsParseException {
		if (!EqualityExpression()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isPunktuator(AND) && BitwiseANDExpression() ) || (
			save.rollback()
		);
	}
	
	private boolean BitwiseANDExpressionNoIn() throws IOException, JsParseException {
		if (!EqualityExpressionNoIn()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isPunktuator(AND) && BitwiseANDExpressionNoIn() ) || (
			save.rollback()
		);
	}
	
	private boolean BitwiseXORExpression() throws IOException, JsParseException {
		if (!BitwiseANDExpression()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() &&  buffer.isPunktuator(XOR) && BitwiseXORExpression() ) || (
			save.rollback()
		);
	}
	
	private boolean BitwiseXORExpressionNoIn() throws IOException, JsParseException {
		if (!BitwiseANDExpressionNoIn()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isPunktuator(XOR) && BitwiseXORExpressionNoIn() ) || (
			save.rollback()
		);
	}
	
	private boolean BitwiseORExpression() throws IOException, JsParseException {
		if (!BitwiseXORExpression()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isPunktuator(OR) && BitwiseORExpression() ) || (
			save.rollback()
		);
	}
	
	private boolean BitwiseORExpressionNoIn() throws IOException, JsParseException {
		if (!BitwiseXORExpressionNoIn()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isPunktuator(OR) && BitwiseORExpressionNoIn() ) || (
			save.rollback()
		);
	}
	
	private boolean LogicalANDExpression() throws IOException, JsParseException {
		if (!BitwiseORExpression()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isPunktuator(ANDAND) && LogicalANDExpression() ) || (
			save.rollback()
		);
	}
	
	private boolean LogicalANDExpressionNoIn() throws IOException, JsParseException {
		if (!BitwiseORExpressionNoIn()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isPunktuator(ANDAND) && LogicalANDExpressionNoIn() ) || (
			save.rollback()
		);
	}
	
	private boolean LogicalORExpression() throws IOException, JsParseException {
		if (!LogicalANDExpression()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isPunktuator(OROR) && LogicalORExpression() ) || (
			save.rollback()
		);
	}
	
	private boolean LogicalORExpressionNoIn() throws IOException, JsParseException {
		if (!LogicalANDExpressionNoIn()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isPunktuator(OROR) && LogicalORExpressionNoIn() ) || (
			save.rollback()
		);
	}
	
	private boolean ConditionalExpression() throws IOException, JsParseException {
		if (!LogicalORExpression()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isPunktuator(QUESTION) && AssignmentExpression() && 
					buffer.isPunktuator(COLON) && AssignmentExpression() ) || (
			save.rollback()
		);
	}
	
	private boolean ConditionalExpressionNoIn() throws IOException, JsParseException {
		if (!LogicalORExpressionNoIn()) {
			return false;
		}
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isPunktuator(QUESTION) && AssignmentExpression() && 
					buffer.isPunktuator(COLON) && AssignmentExpressionNoIn() ) || (
			save.rollback()
		);
	}

	private boolean AssignmentExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		if (!LeftHandSideExpression()) {
			save.rollback();
			return ConditionalExpression();
		}
		SavePoint otherSave = buffer.createSavePoint();
		return (
			otherSave.rollback() && buffer.isPunktuator(ASSIGNMENT) && AssignmentExpression() ) || (
			otherSave.rollback() && AssignmentOperator() && AssignmentExpression() ) || (
			save.rollback() && ConditionalExpression()
		);
	}

	private boolean AssignmentExpressionNoIn() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		if (ConditionalExpressionNoIn()) {
			return true;
		}
		
		if (save.rollback() && !LeftHandSideExpression()) {
			return false;
		}
		save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.isPunktuator(ASSIGNMENT) && AssignmentExpressionNoIn() ) || (
			save.rollback() && AssignmentOperator() && AssignmentExpressionNoIn()
		);
	}

	private boolean AssignmentOperator() throws IOException, JsParseException {
		JsFlexToken token = buffer.next();
		return token != null && (
				token.isSame(ASSIGNMENT_MUL) || token.isSame(ASSIGNMENT_DIV) || token.isSame(ASSIGNMENT_MOD) || 
				token.isSame(ASSIGNMENT_SUM) || token.isSame(ASSIGNMENT_SUB) || token.isSame(ASSIGNMENT_SHL) || 
				token.isSame(ASSIGNMENT_SHR) || token.isSame(ASSIGNMENT_USHR) || token.isSame(ASSIGNMENT_AND) || 
				token.isSame(ASSIGNMENT_XOR) || token.isSame(ASSIGNMENT_OR)
		);
	}

}
