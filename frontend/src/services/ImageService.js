import axios from 'axios';

const UPLOAD_IMAGE_API_BASE_URL = "http://localhost:8080/upload"
const GET_IMAGE_API_BASE_URL = "http://localhost:8080/files"
class ImageService {

    uploadImg(img,tokenStr){
        return axios.post(UPLOAD_IMAGE_API_BASE_URL,img,  {headers: {"Authorization" : `Bearer ${tokenStr}`,"Content-Type" : "multipart/form-data"}});
    };
    
    getImg(imgName ,tokenStr){
        return axios.get(GET_IMAGE_API_BASE_URL, imgName ,  {headers: {"Authorization" : `Bearer ${tokenStr}`,"Content-Type" : "multipart/form-data"}});
    };
    

}   

export default new ImageService();