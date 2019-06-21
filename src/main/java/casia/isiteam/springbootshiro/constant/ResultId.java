package casia.isiteam.springbootshiro.constant;

public interface ResultId {
	enum Account implements ResultId {
		FRIEND_NUM(10), 
		GROUP_NUM(11), 
		FRIEND_UNION_NUM(12), 
		GROUP_UNION_NUM(13), 
		GROUP_MEMBER_NUM(14), 
		FRIEND_FRIEND_NUM(15), 
		CHAT_GROUP_NUM(16), 
		CHAT_GROUP_ACTION_NUM(17), 
		CHAT_ACCOUNT_NUM(18), 
		CHAT_ACCOUNT_ACTION_NUM(19);
		private int code;

		Account(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
	}

	enum Group implements ResultId {
		MEMBER_NUM(20), MEMBER_UNION_NUM(21), MEMBER_GROUP_NUM(22), MEMBER_FRIEND_NUM(23),
		CHAT_NUM(24), CHAT_ACTION_NUM(25);
		private int code;

		Group(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

	}

	enum IP implements ResultId {
		ACCOUNT_NUM(30), GROUP_NUM(31);
		private int code;

		IP(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

	}
}
