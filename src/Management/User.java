package Management;

public class User {
	private String userName;
	private String passWord;
	private String name;
	public User(String user, String pass, String name) {
		userName = user;
		passWord = pass;
		this.name = name;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "User [userName=" + userName + ", passWord=" + passWord + ", name=" + name + "]";
	}
		
}
