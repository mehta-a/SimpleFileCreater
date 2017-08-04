import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amehta02 on 8/4/2017.
 */
public class FileReader {
    static List<String> folderNames;
    static Path rootPath = null;
    static String PDF_FOLDER_NAME = "origpdf";
    static String GEO_FOLDER_NAME = "geofiles";
    static String GEO_EXTENSION = "-geofile.html";

    FileReader() {
        folderNames = new ArrayList<String>() {{add("A");add("B");add("C");}};
        rootPath = Paths.get(System.getProperty("user.dir")); //This is for reference for current path, one can place this as root path
    }

    public static void main(String[] args){
        new FileReader();
        for(String folderName:folderNames) {
            String pdfPath = getPDFFolderPath(folderName);
            File folder = new File(pdfPath);
            if(folder.exists()) {
                File[] listOfFiles = folder.listFiles();
                for (File file : listOfFiles) {
                    String ext = FilenameUtils.getExtension(file.getName());
                    if (file.isFile()&&ext.contains("pdf")) {   //Assuming ther is no pdfx or xpdf extensions.
                        Path destPath = getDestFolderPath(folderName,GEO_FOLDER_NAME);//create dest directory
                        try {
                            if(!Files.exists(destPath)) {
                                Files.createDirectories(destPath);
                            }
                            Path destFile = getDestFileName(destPath.toString(),file.getName());
                            if(new File(destFile.toString()).createNewFile()){
                                System.out.println("File created:"+destFile.toString());
                                continue;
                            }else {
                                System.out.println("Unable to create file");
                            }
                        } catch (IOException e) { //fail to create directory
                            e.printStackTrace();
                        }
                    }
                }
            }else{
                System.out.println("Folder not found:"+folderName);
            }

        }
    }

    private static Path getDestFileName(String destPath, String fileName){
        return Paths.get(destPath.toString(),fileName.replace(".pdf","").replace(" ", "").concat(GEO_EXTENSION));
    }

    private static Path getDestFolderPath(String folderName, String GEO_FOLDER_NAME){
        return Paths.get(rootPath.toString(), folderName, GEO_FOLDER_NAME);
    }

    private static String getPDFFolderPath(String folderName) {
        Path filePath = null;
        if(rootPath!=null) {
            filePath = Paths.get(rootPath.toString(), folderName, PDF_FOLDER_NAME);
        }else{
            System.out.println("RootPath is null");
            return null;
        }
        return filePath.toString();
    }
}
