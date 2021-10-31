import axios from 'axios';

const UPLOAD_IMAGE_API_BASE_URL = "http://localhost:8080/upload"
const IMAGE_API_BASE_URL = "http://localhost:8080/upload"
class ImageService {

    uploadImg(img,tokenStr){
        return axios.post(UPLOAD_IMAGE_API_BASE_URL,img,  {headers: {"Authorization" : `Bearer ${tokenStr}`,"Content-Type" : "multipart/form-data"}});
    };
    


}   

export default new ImageService();