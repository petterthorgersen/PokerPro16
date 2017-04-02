package org.gruppe2.game;

import java.io.Serializable;

/**
 * Get
 */
public abstract class Action implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7227846317512386346L;

	public static class Fold extends Action {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3796715750778514496L;
	}

	public static class Check extends Action {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6999248848752460557L;
	}

	public static class Call extends Action {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1144580862509039790L;
	}

	public static class Raise extends Action {
		/**
		 * 
		 */
		private static final long serialVersionUID = 517243300357626807L;
		private int amount;

		public Raise(int amount) {
			this.amount = amount;
		}

		public int getAmount() {
			return amount;
		}
		
		@Override
		public String toNetworkString() {
			String raiseString = getClass().getSimpleName();
			raiseString = raiseString.concat(";"+String.valueOf(getAmount()));
			return raiseString;
		}
	}

	public static class AllIn extends Action {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8554705323641412270L;
	}

	public static class Blind extends Action {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6981697358231834222L;
		private int amount;

		public Blind(int amount) {
			this.amount = amount;
		}

		public int getAmount() {
			return amount;
		}

		@Override
		public String toString() {
			return "Blind";
		}
	}

	public static class Pass extends Action {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5775745214498059771L;
	}

	public String toNetworkString() {
		return toString();
	}

	@Override	
	public String toString() {
		return getClass().getSimpleName();
	}
}
