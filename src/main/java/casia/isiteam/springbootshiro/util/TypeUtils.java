package casia.isiteam.springbootshiro.util;

public class TypeUtils {
	//获取分析类型
	public static String getAnalysisType(Integer type) {
		switch(type) {
		case 1:
			return "账号碰撞";
		case 2:
			return "群体碰撞";
		case 3:
			return "群聊分析";
		case 4:
			return "私聊分析";
		default:
			return "";
		}
	}
	
	//获取分析数据类型
	public static String getAnalysisDataType(Integer type) {
		switch(type) {
		case 1:
			return "账号好友";
		case 2:
			return "账号资料";
		case 3:
			return "账号加入群";
		case 4:
			return "群成员";
		case 5:
			return "群资料";
		case 6:
			return "账号私聊";
		case 7:
			return "账号群聊";
		case 8:
			return "群号群聊";
		default:
			return "";
		}
	}
	
	//获取分析结果类型
	 public static String getAnalysisResultType(Integer type) {
		 switch(type) {
			case 10:
				//return "好友碰撞-账号好友量分布-样本账号的好友量";
				return "账号好友量分布";
			case 11:
				//return "好友碰撞-账号交互好友分布-样本账号的共同好友量";
				return "账号交互好友分布";
			case 12:
				//return "好友碰撞-好友影响力分布-非样本账号的好友量";
				return "好友影响力分布";
			case 13:
				//return "好友碰撞-账号加入群分布-样本账号加入群数量";
				return "账号加入群分布";
			case 14:
				//return "好友碰撞-账号交互群分布-样本账号共同加入群数量";
				return "账号交互群分布";
			case 15:
				//return "好友碰撞-群成员量分布-群拥有的成员数量";
				return "群成员量分布";
			case 16:
				//return "好友碰撞-账号资料归类-查询账号资料";
				return "账号资料归类";
			case 20:
				//return "群体碰撞-群成员量分布-群拥有的成员数量";
				return "群成员量分布";
			case 21:
				//return "群体碰撞-成员影响力分布-群成员加入的群数量";
				return "成员影响力分布";
			case 22:
				//return "群体碰撞-群交互成员分布-群共同成员数量";
				return "群交互成员分布";
			case 23:
				//return "群体碰撞-群号资料归类";
				return "群号资料归类";
			case 30:
				//return "群聊分析-群号信息量分布-群拥有的信息数量";
				return "群号信息量分布";
			case 31:
				//return "群聊分析-群号线索量分布-群拥有的线索信息数量";
				return "群号线索量分布";
			case 32:
				//return "群聊分析-群成员量分布-群拥有的成员数量";
				return "群成员量分布";
			case 33:
				//return "群聊分析-群号交互成员分布-群共同成员数量";
				return "群号交互成员分布";
			case 34:
				//return "群聊分析-成员影响力分布-成员加入群的数量";
				return "成员影响力分布";
			case 35:
				//return "群聊分析-群成员线索分布-成员发布的线索信息数量";
				return "群成员线索分布";
			case 36:
				//return "群聊分析-群号群聊归类-群聊共性发现归类";
				return "群号群聊归类";
			default:
				return "";
			}
	 }
	 
	 //获取结果分析类型的说明提示
	 public static String getTipByResultId(Integer id) {
		 switch(id) {
			case 10:
				return "* 账号好友量分布：是指根据账号的好友量排序！默认倒序！";
			case 11:
				return "* 账号交互好友分布：是指根据账号拥有共同成员数量统计对群进行排序！默认倒序！";
			case 12:
				return "* 好友影响力分布：是指根据共同好友量进行排序！默认倒序！";
			case 13:
				return "* 账号加入群分布：是指对样本账号根据加入群数量统计排序结果，默认展示倒序！";
			case 14:
				return "* 账号交互群分布：是指对样本账号根据共同加入群数量统计排序结果，默认展示倒序！";
			case 15:
				return "* 账号成员量分布：是指对群根据拥有样本账号数据量统计排序结果，默认展示倒序！";
			case 16:
				return "* 账号资料归类分布：暂无分析！";
			case 20:
				return "* 群成员量分布：是指根据群成员数量统计对群进行排序！默认倒序！";
			case 21:
				return "* 成员影响力分布：是指根据群成员加入的群量统计对群成员进行排序！默认倒序！";
			case 22:
				return "* 群号交互成员分布：是指根据群拥有共同成员数量统计对群进行排序！默认倒序！";
			case 23:
				return "* 群号资料归类：是指根据群资料信息数据共性对群归类统计！";
			case 30:
				return "* 群号信息量分布：是指根据群发布的信息数据量对群统计进行排序！默认倒序！";
			case 31:
				return "* 群号线索量分布：是指根据群发布的线索信息数据量对群统计进行排序！默认倒序！";
			case 32:
				return "* 群成员量分布：是指根据群成员数量对群统计进行排序！默认倒序！";
			case 33:
				return "* 群号交互成员分布：是指根据群拥有共同成员数量统计对群进行排序！默认倒序！";
			case 34:
				return "* 成员影响力分布：是指根据群成员加入的群量统计对群成员进行排序！默认倒序！";
			case 35:
				return "* 群成员线索分布：是指根据群成员发布的线索信息数据量统计对群成员进行排序！默认倒序！";
			case 36:
				return "* 群号群聊归类：是指根据群聊信息数据共性对群归类统计！";
			default:
				return "";
			}
	 }
}
