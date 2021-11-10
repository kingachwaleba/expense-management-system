import axios from 'axios';

const UPLOAD_IMAGE_API_BASE_URL = "/upload"
const GET_IMAGE_API_BASE_URL = "/expense-files/"
class ImageService {

    uploadImg(img,tokenStr){
        return axios.post(UPLOAD_IMAGE_API_BASE_URL,img,  {headers: {"Authorization" : `Bearer ${tokenStr}`,"Content-Type" : "multipart/form-data"}});
    };
    
    getImg(id ,tokenStr){
        return axios.get(GET_IMAGE_API_BASE_URL+id ,  {responseType: 'blob', headers: {"Authorization" : `Bearer ${tokenStr}`}});
    };
    

}   

export default new ImageService();