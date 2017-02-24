package pak;
// obrati paznju na appache.commons biblioteke
// drasticno olaksaju ovaj posao i cine urednijim
// skinuti:
// commons-ioX.X gde je X.X neka verzija, ovde je upotrebljena 2.5
// commons-fileupload1.3.2
// bibloteke je potrebno smestiti i u tomcat lib folder...
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/Upload")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// appache.commons (AC u daljem tekstu) metod : 
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		String msg = "";
		String src = "";
		String alt = "";
		String submit = "";
		String xml = getServletContext().getInitParameter("ImagesFolder");
		if(isMultipart){
			msg  = "jeste multipart...";
			// AC klasa koja ce instancirati "FileItem", a to je upravo ono
			// sto treba da sacuvamo kao fajl, ili pak obradimo kao deo forme
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// standardni java IO i cuvanje...
			File repository = new File(xml);
			// sad gde ce fabrika da cuva fajlove...
			factory.setRepository(repository);
			// API instrument koji ce da primi multipart sa prethodne strane
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				// API vraca citavu listu "stvari", bili to delovi forme ili binarni podaci
				List<FileItem> items = upload.parseRequest(request);
				// mozda je i moglo sa foreach ali preporucujem iterator
				// posto ne mora da se vodi racun kada ce se tacno pojaviti
				// binarni potok podataka
				Iterator<FileItem> iter = items.iterator();
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					if (item.isFormField()) { // AC da li je forma ili ne?
						if (item.getFieldName().equals("submit")) {
							submit += item.getString();
						} else {
							// iterator sadrzi metode za izvlacenje informacija
							String altName = item.getFieldName(); // name
							String altValu = item.getString(); // value
							alt += altValu;
							request.setAttribute("alt", alt);
							msg += altName + " " + altValu + " " + "submit: " + submit;
						}
					} else { // ukoliko nije forma, obradi taj MIME tip
						if (submit.length() >= 0 && item.getName().length() > 0) {
							String fieldName = item.getFieldName();
							String fileName = item.getName();
							String contentType = item.getContentType();
							boolean isInMemory = item.isInMemory();
							long sizeInBytes = item.getSize();
							
							msg += fieldName + " " 
									+ fileName + " " 
									+ isInMemory + " " 
									+ contentType + " "
									+ sizeInBytes;
							// java io
							File uploadedFile = new File(repository.getPath()+ "/" + fileName);
							src += fileName;
							request.setAttribute("src", src);
							// AC metod, snimi fajl...
							item.write(uploadedFile);							
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			
		else
			msg = "nije dobro...";
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("upload.jsp").forward(request, response);
	}

}
