package pak;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class FolderServlet
 */
@WebServlet("/IMAGES/*")
public class FolderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FolderServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// uzmi ime stranice posle localhost:8080, sto znaci ostace samo /s_test.../photo.jpg
		String URLAfterWebDomain = request.getRequestURI();
		
		// izvadi config gde je folder u koji cu smestiti podatke
		// config je u web-inf/web.xml
		String xml = getServletContext().getInitParameter("ImagesFolder");
		
		// izvuci ime slike, klasican substring(int odakleDaUzimam)
		String imgName = URLAfterWebDomain.substring(URLAfterWebDomain.lastIndexOf('/') + 1);
		
		// mala validacija
		if (imgName != null && imgName.length() > 0 && new File(xml + imgName).exists()) {
			// saljemo MIME tip, nesto sto ce biti potok binarnih podataka
			response.setContentType("image/jpeg");
			// evo i izvora
			ServletOutputStream sout = response.getOutputStream();
			// klasicna manipulacija faljlovaima u javi
			FileInputStream fin = new FileInputStream(xml + imgName);
			// baferi zbog razlicitih brzina IO uredjaja,
			BufferedInputStream bin = new BufferedInputStream(fin);
			BufferedOutputStream bout = new BufferedOutputStream(sout);
			int c = 0;
			//  slanje binarnih...
			while ((c = bin.read()) != -1) {
				bout.write(c);
			}
			bin.close();
			bout.close();
			fin.close();
			sout.close();
		} 
	}
}
