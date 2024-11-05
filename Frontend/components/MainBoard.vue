<template>
    <div class="mainboard">
        <div v-if="listing === 'Emails'" class="email-list">
            <div class="email" v-for="email in paginatedElements" :key="email" @click="openMail(email.id)">
                <div v-if="multiselect" class="checkbox">
                    <input type="checkbox" :value="email.id" @click.stop="selectEmail(email)" v-model="checkedEmails">
                </div>
                <div class="user">
                    <div v-for="line in getEmailUser(email)" :key="line.value">
                        <label style="font-weight: bold;cursor: pointer;">{{ line.label }}</label>
                        <i>{{ line.value }}</i>
                    </div>
                </div>
                <div class="content">
                    <i><label style="font-weight: bold;cursor: pointer;">Subject: </label>{{ chopString(email.subject, 25)
                    }}</i>
                    <i><label style="font-weight: bold;cursor: pointer;">Body: </label>{{ chopString(email.body, 85) }}</i>
                </div>
                <div class="date">{{ email.date }}</div>
                <div v-if="!multiselect" class="emailOptions">
                    <i class="pi pi-folder-open moveEmail icon" @click.stop="moveEmail(email)"></i>
                    <i class="pi pi-trash deleteEmail icon" @click.stop="deleteEmail(email)"></i>
                </div>
            </div>
        </div>
        <div v-else-if="listing === 'Contacts'" class="contacts-list">
            <div class="contact" v-for="contact in paginatedElements" :key="contact">
                <div class="contactTitle">
                    <i class="pi pi-user" style="font-size: 3vh;"></i>
                    <div class="contactName" @click="this.$emit('RenameContact', contact)">
                        {{ contact.name }}
                    </div>
                    <i class="pi pi-plus addEmail icon" @click.stop="this.$emit('AddEmail', contact)"></i>
                    <i class="pi pi-times deleteContact icon" @click.stop="this.$emit('DeleteContact', contact)"></i>
                </div>
                <div class="contactEmailList">
                    <div class="contactEmail" v-for="email in contact.emails" :key="email">
                        <i class="contactEmailName">{{ email }}</i>
                        <i class="pi pi-trash deleteContactEmail icon"
                            @click.stop="this.$emit('DeleteContactEmail', contact, email)"></i>
                        <i class="pi pi-file-edit renameContactEmail icon"
                            @click.stop="this.$emit('RenameContactEmail', email)"></i>
                    </div>
                </div>
            </div>
            <div v-if="this.currentPage == totalPages || this.Contacts.length == 0" class="addContact">
                <button class="pi pi-plus addContactbtn" @click="this.$emit('AddContact')"></button>
            </div>
        </div>
        <div class="pagination">
            <button class="pi pi-angle-left paginationBtn" @click="previousPage" :disabled="currentPage === 1"></button>
            <div class="page-numbers">
                <button v-for="pageNumber in displayedPageNumbers" :key="pageNumber" class="nmbrBtn"
                    :class="{ active: pageNumber === currentPage }" @click="goToPage(pageNumber)">{{ pageNumber }}</button>
            </div>
            <button class="pi pi-angle-right paginationBtn" @click="nextPage"
                :disabled="currentPage === totalPages"></button>
        </div>
    </div>
</template>

<script>
export default {
    name: 'MainBoard',
    props: ['emails', 'currentFolder', 'selectedEmails', 'multiselect', 'listing', 'Contacts'],
    data() {
        return {
            checkedEmails: [],
            selEmails: [],
            currentPage: 1,
            EmailsPerPage: 10,
            ContactsPerPage: 8,
        };
    },
    watch: {
        multiselect() {
            this.checkedEmails = [];
            this.selEmails = [];
        }
    },
    computed: {
        paginatedElements() {
            if (this.listing === 'Emails') {
                const startIndex = (this.currentPage - 1) * this.EmailsPerPage;
                const endIndex = startIndex + this.EmailsPerPage;
                return this.emails.slice(startIndex, endIndex);
            }
            else if (this.listing === 'Contacts') {
                const startIndex = (this.currentPage - 1) * this.ContactsPerPage;
                const endIndex = startIndex + this.ContactsPerPage;
                return this.Contacts.slice(startIndex, endIndex);
            }
            else return [];
        },
        totalPages() {
            if (this.listing === 'Emails')
                return Math.ceil(this.emails.length / this.EmailsPerPage);
            else if (this.listing === 'Contacts') {
                if (this.Contacts.length % this.ContactsPerPage == 0) {
                    return (Math.ceil(this.Contacts.length / this.ContactsPerPage) + 1);
                }
                else return Math.ceil(this.Contacts.length / this.ContactsPerPage);
            }
            else return [];
        },
        displayedPageNumbers() {
            const range = 10;
            const startPage = Math.max(1, this.currentPage - Math.floor(range / 2));
            const endPage = Math.min(this.totalPages, startPage + range - 1);
            return Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i);
        },
    },
    methods: {
        previousPage() {
            if (this.currentPage > 1) {
                this.currentPage--;
            }
        },
        nextPage() {
            if (this.currentPage < this.totalPages) {
                this.currentPage++;
            }
        },
        goToPage(pageNumber) {
            this.currentPage = pageNumber;
        },
        getEmailUser(email) {
            if (this.currentFolder === 'Inbox') {
                return [{ label: 'From: ', value: email.from }];
            } else if (this.currentFolder === 'Sent' || this.currentFolder === 'Draft') {
                return [{ label: 'To: ', value: email.to }];
            } else{
                return [{ label: 'From: ', value: email.from }, { label: 'To:', value: email.to }];
            }
        },
        chopString(string, length) {
            if (string.length > length) {
                return `${string.slice(0, length)}...`;
            } else {
                return string;
            }
        },
        selectEmail(email) {
            const index = this.checkedEmails.indexOf(email.id);
            if (index === -1) {
                this.checkedEmails.push(email.id);
            } else {
                this.checkedEmails.splice(index, 1);
            }
            this.checkedEmails = this.checkedEmails.filter(id => {
                return this.emails.find(email => email.id === id);
            });
            this.selEmails = this.checkedEmails.map(id => {
                return this.emails.find(email => email.id === id);
            });
            console.log('selected emails in mainboard');
            console.log(this.selEmails);
            this.$emit('update:selectedEmails', this.selEmails);
        },
        openMail(id){
            if(this.currentFolder==='Draft')
                this.$emit('openDraft', id);
            else
                this.$emit('openMail', id);
        },
        moveEmail(email) {
            this.selEmails.push(email)
            console.log(this.selEmails)
            this.$emit('moveEmail', this.selEmails)
            this.selEmails = []
        },
        deleteEmail(email) {
            this.selEmails.push(email);
            this.$emit('deleteEmail', this.selEmails);
            this.selEmails = [];
        }
    }
}
</script>

<style scoped>
.mainboard {
    background-color: rgba(7, 10, 10, 0.94);
    height: 85.1vh;
    width: 80vw;
    margin: 0;
    border-bottom: 0.1vw solid black;
}

.email-list {
    height: 90%;
    width: 100%;
    justify-content: flex-start;
    align-items: center;
    padding-left: 1vw;
    padding-top: 1.5vh;
}

.email {
    background-color: rgba(127, 129, 132, 0.821);
    height: 6.5vh;
    width: 95%;
    margin-bottom: 1vh;
    border-radius: 10px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    padding-left: 1vw;
    font-size: 2vh;
    color: black;
}

.email:hover {
    background-color: rgba(127, 129, 132, 0.5);
    color: white;
    cursor: pointer;
}

.user {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    width: 20%;
    height: 100%;
    margin-right: 1vw;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.content {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    width: 55%;
    height: 100%;
    margin-right: 1vw;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.date {
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 8%;
    height: 40%;
}

.emailOptions {
    position: relative;
    left: 5.5vw;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    width: 5%;
    height: 100%;
    color: black;
}

.moveEmail {
    font-size: 3.1vh;
    padding: 1vh;
}

.deleteEmail {
    font-size: 3vh;
    padding: 1vh;
}

.moveEmail:hover {
    color: teal;
}

.deleteEmail:hover {
    color: red;
}

.email .emailOptions {
    display: none;
}

.email:hover .emailOptions {
    display: flex;
}

.checkbox {
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 5%;
    height: 40%;
}

.pagination {
    margin-top: 2vh;
}

.paginationBtn {
    background-color: transparent;
    border: none;
    color: white;
    font-size: 3vh;
    ;
    cursor: pointer;
}

.paginationBtn:disabled {
    color: grey;
}

.page-numbers {
    display: inline-flex;
    font-size: 1vh;
    transform: translate(0, -0.5vh);
}

.nmbrBtn {
    background-color: transparent;
    border: none;
    color: white;
    font-size: 2vh;
    cursor: pointer;
}

.nmbrBtn.active {
    color: grey;
}

.contacts-list {
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
    align-items: flex-start;
    height: 90%;
    width: 100%;
    padding: 1vw;
}

.contact {
    background-color: rgb(118, 113, 156);
    width: 23.8%;
    height: 35vh;
    margin: 1vh;
    border-radius: 1vw;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;
    padding: 1vw;
}

.addContact {
    width: 23.8%;
    height: 35vh;
    margin: 1vh;
    border-radius: 1vw;
}

.addContactbtn {
    background-color: rgba(62, 65, 68, 0.763);
    border-radius: 2vw;
    color: rgba(9, 10, 10, 0.94);
    font-size: 6vh;
    font-weight: bold;
    cursor: pointer;
    height: 100%;
    width: 100%;
}

.addContactbtn:hover {
    background-color: rgba(22, 29, 29, 0.94);
}

.contactTitle {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    width: 100%;
    margin-bottom: 1vh;
    border-bottom: 0.3vh solid black;
    color: black;
}

.contactName {
    padding-top: 0.5vh;
    text-align: left;
    width: 85%;
    font-size: 2.5vh;
    font-weight: bold;
    margin-left: 0.4vw;
    overflow: hidden;
}

.contactEmailList {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;
    width: 100%;
    height: 85%;
    margin-top: 1vh;
    overflow-y: auto;
    overflow-x: hidden;
}

.contactEmail {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
    width: 100%;
    height: 10%;
    margin-bottom: 0.5vh;
    border-bottom: 0.1vh solid black;
    color: black;
    padding: 1vh;
}

.contactEmailName {
    text-align: left;
    width: 85%;
    font-size: 2vh;
    margin-left: 0.4vw;
    overflow: hidden;
}

.deleteContactEmail {
    font-size: 2vh;
    padding: 0.5vh;
    color: black;
    cursor: pointer;
    font-weight: bold;
}

.renameContactEmail {
    font-size: 2vh;
    padding: 0.5vh;
    color: black;
    cursor: pointer;
    font-weight: bold;
}

.deleteContactEmail:hover {
    color: red;
}

.renameContactEmail:hover {
    color: green;
}

.contact .deleteContactEmail {
    display: none;
}

.contact:hover .deleteContactEmail {
    display: block;
}

.contact .renameContactEmail {
    display: none;
}

.contact:hover .renameContactEmail {
    display: block;
}

.deleteContact {
    padding: 0.5vh;
    font-size: 2vh;
    color: black;
    cursor: pointer;
    font-weight: bold;
}

.addEmail {
    padding: 0.5vh;
    font-size: 2vh;
    color: black;
    cursor: pointer;
    font-weight: bold;
}

.deleteContact:hover {
    color: red;
}

.addEmail:hover {
    color: green;
}

.contact .deleteContact {
    display: none;
}

.contact:hover .deleteContact {
    display: block;
}

.contact .addEmail {
    display: none;
}

.contact:hover .addEmail {
    display: block;
}

.contactEmail {
    text-align: left;
    width: 95%;
    height: 10%;
    font-size: 2vh;
    margin-bottom: 0.5vh;
}

::-webkit-scrollbar {
    width: 0.5vw;
    background-color: transparent;
}

::-webkit-scrollbar-thumb {
    background-color: rgba(3, 4, 5, 0.175);
    border-radius: 0.5vw;
}</style>