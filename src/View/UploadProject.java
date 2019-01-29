package View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import Management.Driver;
/**
 * Servlet implementation class Upload
 */
@WebServlet("/Upload")
@MultipartConfig
public class UploadProject extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadProject() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pName = request.getParameter("projectName");
		System.out.println(pName);
		String allauthors= request.getParameter("authors");
		System.out.println(allauthors);
		String pdes = request.getParameter("pdescriptions");
		System.out.println(pdes);
		String vdes = request.getParameter("vdescriptions");	
		System.out.println(vdes);
		String[] authors = new String[] {allauthors};
		if(allauthors.contains(","))
			authors = allauthors.split(",");
		String pid = request.getParameter("pid");
		String username = request.getParameter("username");
		Part filePart = request.getPart("prj"); // Retrieves <input type="file" name="file">
	    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
	    InputStream fileContent = filePart.getInputStream();

		File targetFile = new File(getServletContext().getRealPath("/")+"\\res\\"+"new.zip");
		byte[] buffer = new byte[fileContent.available()];
		fileContent.read(buffer);

		OutputStream outStream = new FileOutputStream(targetFile);
		outStream.write(buffer);
		outStream.close();
		fileContent.close();
		if (!ZipManagement.Unzip.unzip(getServletContext().getRealPath("/") + "\\res\\new.zip",
				getServletContext().getRealPath("/") + "\\res\\new")) {
			System.out.println("unable to unzip the file");
			return;
		}
	    Driver driver = new Driver(getServletContext().getRealPath("/"+username));
	    driver.addProject(pName, getServletContext().getRealPath("/")+"\\res\\new", authors, pdes, vdes);
	    targetFile.delete();
		doGet(request, response);
	}

}
