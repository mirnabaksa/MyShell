package hr.fer.zemris.java.hw06.shell;
/**
 * Implements shell statuses.
 * 
 * @see ShellStatus#CONTINUE
 * @see ShellStatus#TERMINATE
 * @author Mirna Baksa
 *
 */
public enum ShellStatus {
	/**
	 * Marks that the shell work is to be continued.
	 */
	CONTINUE,
	/**
	 * Marks that the shell needs to be terminated.
	 */
	TERMINATE;
}
