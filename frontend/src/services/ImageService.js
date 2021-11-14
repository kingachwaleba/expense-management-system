import axios from 'axios';

const UPLOAD_IMAGE_API_BASE_URL = "/upload"
const GET_IMAGE_API_BASE_URL = "/expense-files/"
const GET_PROFILE_IMAGE_API_BASE_URL = "/user-files/"
const SET_USER_PROFILE_PICTURE = "/account/change-profile-picture"
class ImageService {

    uploadImg(img,tokenStr){
        return axios.post(UPLOAD_IMAGE_API_BASE_URL,img,  {headers: {"Authorization" : `Bearer ${tokenStr}`,"Content-Type" : "multipart/form-data"}});
    };
    
    getImg(id ,tokenStr){
        return axios.get(GET_IMAGE_API_BASE_URL+id ,  {responseType: 'blob', headers: {"Authorization" : `Bearer ${tokenStr}`}});
    };
    
    setUserProfileImg(img,tokenStr){
        return axios.put(SET_USER_PROFILE_PICTURE,img,  {headers: {"Authorization" : `Bearer ${tokenStr}`,"Content-Type" : "multipart/form-data"}});
    }

    getUserProfileImg(login,tokenStr){
        return axios.get(GET_PROFILE_IMAGE_API_BASE_URL+login ,  {responseType: 'blob', headers: {"Authorization" : `Bearer ${tokenStr}`}});
    }

}   

export default new ImageService();