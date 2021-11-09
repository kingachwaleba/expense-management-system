import axios from "axios";
const INVITATIONS_API_BASE_URL = "/notifications/invitations";
const INVITATIONS_MANAGE_API_BASE_URL = "/notifications/invitations/";

const DEBT_REMINDER_API_BASE_URL ="/send-notification/wallet/"
const DEBT_REMINDERS_NOTIFICATION_API_BASE_URL ="/debts-notifications"
class NotificationService {

    getNotifications(tokenStr){
        return axios.get(INVITATIONS_API_BASE_URL,  {headers: {"Authorization" : `Bearer ${tokenStr}`}});
    }
    getReminders(tokenStr){
        return axios.get(DEBT_REMINDERS_NOTIFICATION_API_BASE_URL,  {headers: {"Authorization" : `Bearer ${tokenStr}`}});
    }
    responseToNotification(id, flag, tokenStr){
        return axios.put(INVITATIONS_MANAGE_API_BASE_URL+id, flag , {headers: {"Authorization" : `Bearer ${tokenStr}`, "Content-Type" : "application/json"}});
    }
    sendReminder(id, debtHolder, tokenStr){
        return axios.post(DEBT_REMINDER_API_BASE_URL+id, debtHolder, {headers: {"Authorization" : `Bearer ${tokenStr} `, "Content-Type" : "application/json"}});
    }
}

export default new NotificationService();