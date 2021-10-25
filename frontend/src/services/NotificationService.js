import axios from "axios";
const INVITATIONS_API_BASE_URL = "http://localhost:8080/notifications/invitations";
const INVITATIONS_MANAGE_API_BASE_URL = "http://localhost:8080/notifications/invitations/";
class NotificationService {

    getNotifications(tokenStr){
        return axios.get(INVITATIONS_API_BASE_URL,  {headers: {"Authorization" : `Bearer ${tokenStr}`}});
    }

    responseToNotification(id, flag, tokenStr){
        return axios.put(INVITATIONS_MANAGE_API_BASE_URL+id, flag , {headers: {"Authorization" : `Bearer ${tokenStr}`, "Content-Type" : "application/json"}});
    }

}

export default new NotificationService();