package com.auction.app.upload;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CloudinaryUploadService {

    // Cloudinary cloud_name, API_Key and API_Secret
    private static final String CLOUDINARY_CLOUD_NAME = "kkraljevic";
    private static final String CLOUDINARY_API_KEY = "555473889443414";
    private static final String CLOUDINARY_API_SECRET = "46lG4apQ5T08ciyBTbn21a7MxBo";

    public List<String> storeFiles(List<MultipartFile> files){
        List<String> imgURLs=new ArrayList<>();

        for (MultipartFile file: files) {

            try {
                imgURLs.add(storeFileCloudinary(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imgURLs;
    }

    public String storeFileCloudinary(MultipartFile multipartFile) throws IOException {
    Cloudinary cloudinary = getCloudinaryClient();

    String fileName="";

    Map<String, Object> cloudinaryURL = null;

		if(multipartFile!=null){
        fileName = multipartFile.getOriginalFilename();
        cloudinaryURL = uploadToCloudinary(cloudinary,multipartFile);
    }
        String urlClodinary =(String)cloudinaryURL.get("public_id");
		System.out.println("Clodinary URL: "+urlClodinary);

    // apply Transformation on the uploaded image and get the url from Cloudinary cloud
    String url = cloudinary.url().generate("abh/auction/"+multipartFile.getOriginalFilename());

		System.out.println("Transformation URL: "+url);

		return cloudinaryURL.get("secure_url").toString();

}

    private static Cloudinary getCloudinaryClient() {

        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUDINARY_CLOUD_NAME,
                "api_key", CLOUDINARY_API_KEY,
                "api_secret", CLOUDINARY_API_SECRET,
                "secure", true));
    }

    public static Map<String, Object> uploadToCloudinary(Cloudinary cloudinary,MultipartFile sourceFile)throws IOException {

        Map<String, Object> cloudinaryUrl = null;
        Map params = ObjectUtils.asMap("public_id", "abh/auction/"+sourceFile.getOriginalFilename());


        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> result = (Map<String, Object>)cloudinary.uploader().upload(sourceFile.getBytes(),params);
            cloudinaryUrl = result;
        } catch (IOException e) {
            System.out.println("Could not upload file to Cloundinary from MultipartFile " + sourceFile.getOriginalFilename()+ e.toString());
            throw e;
        }

        return cloudinaryUrl;
    }

}
