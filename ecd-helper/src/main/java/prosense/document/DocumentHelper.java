package prosense.document;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.File;
import java.io.FileInputStream;
 
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;


public class DocumentHelper {
	// DEV
//     private static final String source = "/u01/ecd/GovChat";
	// PROD
    private static final String source = "/ecd/files/GovChat";
    private static final String dir1 = "ecd-file-uploads";
    private static final String dir2 = "ecd-uploads";
    private static final String dir3 = "ecd-file-uploads-unstructured";
    private static final String regDir = "registrationprooflink";
    
    private static final String uploadResource = "http://localhost:7003/ecd/api/documents/upload";

    private static int appCount;

    public static void main(String[] args) {
    	System.out.println("main start");
    	if (!Files.exists(Paths.get(source)))
    		System.out.println("source directory - " + source + " - does not exist");
    	else
	    	Arrays.stream(args).forEach(arg -> process(arg, "reg", regDir));
    	System.out.println("main end");
    }

    private static void process(final String fileName, final String type, final String dir) {
    	System.out.println(fileName + " - start");
    	appCount = 0;
		try {
			List<String> appIds = Files.readAllLines(Paths.get(fileName));
			appIds.forEach(appId -> {
				appCount++;
				try {
					if (Files.exists(Paths.get(source + "/" + dir1 + "/" + appId + "/" + dir))) {
						if (Files.list(Paths.get(source + "/" + dir1 + "/" + appId + "/" + dir)).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList()).size() > 0)
							upload(dir1, Files.list(Paths.get(source + "/" + dir1 + "/" + appId + "/" + dir)).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList()).get(0), appId, type);
					} else if (Files.exists(Paths.get(source + "/" + dir2 + "/" + appId + "/" + dir))) {
						if (Files.list(Paths.get(source + "/" + dir2 + "/" + appId + "/" + dir)).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList()).size() > 0)
							upload(dir2, Files.list(Paths.get(source + "/" + dir2 + "/" + appId + "/" + dir)).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList()).get(0), appId, type);
					} else if (Files.exists(Paths.get(source + "/" + dir3 + "/" + appId))) {
						if (Files.list(Paths.get(source + "/" + dir3 + "/" + appId)).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList()).size() > 0)
							upload(dir3, Files.list(Paths.get(source + "/" + dir3 + "/" + appId)).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList()).get(0), appId, type);
					} else
						System.out.println(appCount + " - " + appId + " - not found");
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
    	System.out.println(fileName + " - total => " + appCount + " - end");
    }

	private static void upload(final String dir, final File file, final String appId, final String type) {
        try {
			MultipartFormDataOutput mdo = new MultipartFormDataOutput();
			mdo.addFormData("file", new FileInputStream(file), MediaType.valueOf(file.toURL().openConnection().getContentType()));
			mdo.addFormData("appId", appId, MediaType.TEXT_PLAIN_TYPE);
			GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(mdo) { };
            ResteasyClient client = (ResteasyClient) ResteasyClientBuilder.newBuilder().build();
            Response response = client.target(uploadResource).path(type).request()
            					.header("user", "dochelper")
            					.post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA));
            if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL)
                throw new RuntimeException(response.readEntity(String.class));
			System.out.println(appCount + " - " + appId + " - success - " + " - " + dir + " - " + file.getName());
			client.close();
        } catch(Exception e){
			System.out.println(appCount + " - " + appId + " - error - " + e.getMessage() + " - " + dir + " - " + file.getName());
        }
	}
}