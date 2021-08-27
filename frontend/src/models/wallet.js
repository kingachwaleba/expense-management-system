export class Wallet {

    constructor(id, name, description, walletCategory, owner, userList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.walletCategory = walletCategory;
        this.owner = owner;
        this.userList = userList;
    }
}