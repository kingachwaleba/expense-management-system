import axios from 'axios';

const MESSAGES_API_BASE_URL = "/wallet/"

class ChatService {

    getMessages(id, date ,tokenStr){
        return axios.get(MESSAGES_API_BASE_URL+id+"/message/"+date,  {headers: {"Authorization" : `Bearer ${tokenStr}`,"Content-Type" : "multipart/form-data"}});
    };
    sendMessage(id, message,tokenStr){
        return axios.post(MESSAGES_API_BASE_URL+id+"/message/",message,  {headers: {"Authorization" : `Bearer ${tokenStr}`, "Content-Type" : "application/json"}});
    }
   

}   

export default new ChatService();