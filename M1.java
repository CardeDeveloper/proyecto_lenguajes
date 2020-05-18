import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class M1 {

	static boolean error = false;// bandera por si ocurre un error, por el momento para ser fiel al pseudocódigo

	public static boolean isNumber(char c) {
		if (c >= '0' && c <= '9')
			return true;
		else
			return false;

	}

	public static boolean isVariable(char c) {
		if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c == 'á') || (c == 'é') || (c == 'í') || (c == 'ó')
				|| (c == 'ú') || (c == 'Á') || (c == 'É') || (c == 'Í') || (c == 'Ó') || (c == 'Ú') || (c == ' ') || (c == '\\')
				|| (c == '\n') || (c == '&'))
			return true;
		else
			return false;

	}

	public static boolean isLeftParenthesis(char c) {
		if (c == '(')
			return true;
		else
			return false;
	}

	public static boolean isRightParenthesis(char c) {
		if (c == ')')
			return true;
		else
			return false;
	}

	public static int isAOperator(char c) { // precencia
		if (c == '|')
			return 0;// + Unión tiene la menor precedencia
		else if (c == '-')
			return 1; // - concatenación
		else if (c == '+')
			return 2; // + cerradura positiva
		else if (c == '*')
			return 3; // * cerradura Kleene
		else
			return -1;
	}

	public static String infixToPostfixNotation(String s) {

		Stack<Character> pila = new Stack<Character>();
		List<Character> entrada = new ArrayList<Character>();
		List<Character> salida = new ArrayList<Character>();

		char[] charArray = s.toCharArray();

		for (int x = 0; x < charArray.length; x++) {
			entrada.add(charArray[x]);
		}

		while (entrada.isEmpty() == false && error == false) {
			char caso = entrada.remove(0);

			if (isNumber(caso)) {
				salida.add(caso);
			} else if (isVariable(caso)) {
				salida.add(caso);
			} else if (isLeftParenthesis(caso)) {
				pila.add(caso);
			} else if (isRightParenthesis(caso)) {
				while (pila.isEmpty() == false && !(isLeftParenthesis(pila.peek()))) {
					salida.add(pila.pop());
				}
				if (isLeftParenthesis(pila.peek())) {
					pila.pop();
				} else
					error = true;
			} else if (isAOperator(caso) != -1) {
				while (pila.isEmpty() == false && isAOperator(pila.peek()) >= isAOperator(caso)) {
					salida.add(pila.pop());
				}
				pila.add(caso);
			}

		}

		while (!pila.isEmpty()) {
			salida.add(pila.pop());
		}

		String postfixNotation = "";

		while (!salida.isEmpty()) {
			postfixNotation += salida.get(0);
			salida.remove(0);
		}

		return postfixNotation;

	}

}
