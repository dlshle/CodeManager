package View;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import MainLogic.Document.Document;
import MainLogic.VersionControl.Version;
import MainLogic.VersionControl.VersionIndex;
import Management.Driver;
import Management.User;

/**
 * Servlet implementation class View
 */
@WebServlet("/View")
public class Dispatcher extends HttpServlet {
	private static final long serialVersionUID = 1L;    
    private User user;
    private Driver driver;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Dispatcher() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public VersionIndex getProject(int projectId) {
    	if(user == null)
    		return null;
    	VersionIndex project = driver.getProject(projectId);
    	return project;
    }
    
    public Version getVersion(int vIndex, int vId) {
    	if(user==null)
    		return null;
    	Version version = driver.getVersion(driver.getProject(vIndex), vId);
    	return version;
    }
    
    public Document getDocument(int pid, int vid, int did) {
    	if(user==null)
    		return null;
    	Document d = driver.getDocument(driver.getVersion(driver.getProject(pid), vid), did);
    	return d;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String viewDocument = request.getParameter("viewDocument");
		//projectid, versionid, document id
		String[] viewDocumentInfo;
		String viewProject = request.getParameter("viewProject");
		//versionindex id, version id
		String[] viewProjectInfo;
		String viewVersion = request.getParameter("viewVersion");
		RequestDispatcher requestDispatcher = null;
		VersionIndex project = null;
		Version version = null;
		Document document = null;
		
		request.setAttribute("user", user);
		if(viewProject!=null&&!viewProject.isEmpty()) {
			project=getProject(Integer.valueOf(viewProject));
			if(project!=null) {
			request.setAttribute("project", project);
			requestDispatcher = request.getRequestDispatcher("project.jsp");
			}else
				requestDispatcher = request.getRequestDispatcher("error.jsp");
		}else if(viewVersion!=null&&!viewVersion.isEmpty()){
			viewProjectInfo=viewVersion.split(",");
			version = getVersion(Integer.valueOf(viewProjectInfo[0]),Integer.valueOf(viewProjectInfo[1]));
			if(version != null) {
			request.setAttribute("version", version);
			requestDispatcher = request.getRequestDispatcher("version.jsp");
			}else
				requestDispatcher = request.getRequestDispatcher("error.jsp");
		}else if(viewDocument!=null&&!viewDocument.isEmpty()){
			viewDocumentInfo = viewDocument.split(",");
			document = getDocument(Integer.valueOf(viewDocumentInfo[0]),Integer.valueOf(viewDocumentInfo[1]),Integer.valueOf(viewDocumentInfo[2]));
			if(document!=null) {
				request.setAttribute("document", document);
				Version dv = driver.getVersion(driver.getProject(Integer.valueOf(viewDocumentInfo[0])), Integer.valueOf(viewDocumentInfo[1]));
				String documentPath = dv.getProjectName()+"/"+dv.getVersion()+"/"+document.getFileName();
				String realPath = user.getUserName()+"/"+dv.getProjectId()+"/"+dv.getVersionId()+"/"+document.getFileName();
				request.setAttribute("documentPath", documentPath);
				request.setAttribute("realPath", realPath);
				requestDispatcher = request.getRequestDispatcher("document.jsp");				
			}else
				requestDispatcher = request.getRequestDispatcher("error.jsp");
		}else {
			requestDispatcher = request.getRequestDispatcher("error.jsp");
		}		
		
		requestDispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter("username");
		String passWord = request.getParameter("password");

		user = new User(userName, passWord, userName);
		
		request.setAttribute("user",user);

		String path = getServletContext().getRealPath("/");
		request.setAttribute("dir", path);
		
		System.out.println(getServletContext().getRealPath("/"+userName));
		driver = new Driver(getServletContext().getRealPath("/"+userName));
		
		String[] projects = new String[driver.getNumberOfProjects()];
		driver.getProjectNames().toArray(projects);
		
		request.setAttribute("projects", projects);
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("view.jsp");
		
		requestDispatcher.forward(request, response);
	}

}
