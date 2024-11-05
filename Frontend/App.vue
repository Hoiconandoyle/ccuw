<template>
  <WelcomePage v-if="!userLoggedIn" @login="login" @signUp="signUp" :WelcomeMsg="WelcomeMsg" @updateMessage="WelcomeMsg='' />
  <div v-else>
    <div class="Title">
      <span style="position: absolute; left: 0.1vw;margin-top: 0.2vh;">
        <i class="pi pi-user icon" style="font-size: 2.8vh;"></i>
        <i style="font-size: 2vh;margin-left: 0.2vw;">{{ username }}</i>
      </span>
      <span style="margin: auto;cursor: default;">ֆmail</span>
      <span style="position: absolute; right: 0.1vw;">
        <button class="logout" @click="logout">
          <i class="pi pi-sign-out" style="font-size: 2vh;"></i>
          Logout
        </button>
      </span>
    </div>
    <NavBar :currentFolder="currentFolder" :multiselect="multiselect" :composing="composing"
      @toggleMultiSelect="toggleMultiSelect" @compose="compose" @sort="sort"
      @moveEmail="moveEmail(this.selectedEmails)" @deleteEmail="deleteEmail(this.selectedEmails)"
      @open="open(this.currentFolder)" @search="search" />
    <div style="display: flex;">
      <SideBar :currentFolder="currentFolder" :useremail="useremail" @open="open" @openContacts="openContacts"
        @openDraft="openDraft" :Folders="Folders" />
      <div>
        <MainBoard v-if="!composing && windowState === 'viewFolders'" :Contacts="Contacts" :listing="listing"
          :emails="emails" :currentFolder="currentFolder" :multiselect="multiselect" @openMail="openMail"
          @openDraft="openDraft" @moveEmail="moveEmail" @deleteEmail="deleteEmail" @AddContact="AddContact"
          @AddEmail="AddEmail" @DeleteContactEmail="DeleteContactEmail" @RenameContactEmail="RenameContactEmail"
          @RenameContact="RenameContact" @DeleteContact="DeleteContact" v-model:selectedEmails="selectedEmails">
        </MainBoard>
        <ViewMail v-else-if="!composing && windowState === 'viewMail'" @closeViewMail="windowState = 'viewFolders'"
          :email="email" :useremail="useremail" :currentFolder="currentFolder" />
        <ComposeWindow v-else-if="composing || windowState === 'viewDraft'" @closeWindow="closeCompose"
          :useremail="useremail" :email="email" :windowState='windowState' @open="open" @test="test"/>
      </div>
    </div>
    <footer>
      ©2023 OOP Assingment 4
    </footer>
  </div>
</template>

<script>
import MainBoard from './components/MainBoard.vue';
import NavBar from './components/NavBar.vue';
import SideBar from './components/SideBar.vue';
import WelcomePage from './components/WelcomePage.vue';
import ComposeWindow from './components/ComposeWindow.vue';
import ViewMail from './components/ViewMail.vue';
import 'primeicons/primeicons.css';

export default {
  name: "App",
  components: { WelcomePage, NavBar, SideBar, MainBoard, ComposeWindow, ViewMail },
  data() {
    return {
      username: '',
      useremail: '',
      userLoggedIn: false,
      WelcomeMsg: '',
      currentFolder: 'Inbox',
      emails: [],
      email: {
        id: '',
        from: '',
        to: '',
        subject: '',
        body: '',
        date: '',
        priority: '',
        attachments: []
      },
      multiselect: false,
      selectedEmails: [],
      composing: false,
      windowState: 'viewFolders',
      Contacts: [],
      Folders:[],
      listing: 'Emails',
      addedFolder: ''
    }
  },
  methods: {
    login(useremail,password) {
      const userRequest = {
        email: useremail,
        password: password,
      };
      console.log(userRequest)
      fetch('http://localhost:8080/user/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userRequest),
      })
        .then(res => res.text())
        .then(data => {
          console.log(data)
          if (data === 'true') {
            this.getUsername(useremail);
            this.useremail = useremail;
            this.userLoggedIn = true;
            this.WelcomeMsg = '';
            this.open('Inbox');
          } else {
            this.WelcomeMsg = 'Invalid email or password!';
          }
        })
    },
    signUp(username, useremail,password) {
      const userRequest = {
        userName: username,
        email: useremail,
        password: password,
      };
      fetch('http://localhost:8080/user/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userRequest),
      })
        .then(res => res.json())
        .then(data => {
          // 400 bad request for validation
          if (data.errors) {
            this.WelcomeMsg = data.errors[0].defaultMessage;
          } else if(data.message){
            // run time exception error 500 when user already exist
            this.WelcomeMsg = data.message;
          }
          else{
            this.WelcomeMsg = 'User created successfully!';
          }
        })
        .catch(error => console.error('Error:', error));
    },
    getUsername(useremail) {
      fetch('http://localhost:8080/' + useremail, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
      })
        .then(res => res.json())
        .then(data => {
          console.log(data)
          this.username = data.userName;
          console.log(data.folders)
          let tempFolders=[];

          for(let folder of data.folders){
             tempFolders.push({name:folder.name})
            console.log(folder)
          }
          this.Folders=tempFolders
          console.log("hana "+this.Folders[1].name)

        })
        .catch(error => console.error('Error:', error));
    },
    logout() {
      this.username = '';
      this.useremail = '';
      this.userLoggedIn = false;
    },
    open(folder) {
      this.currentFolder = folder;
      this.emails = [];
      this.selectedEmails = [];
      this.listing = 'Emails';
      this.closeCompose();
      fetch('http://localhost:8080/' + this.useremail + '/' + folder.toLowerCase(), {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
      })
        .then(res => res.json())
        .then(data => {
          console.log(JSON.stringify(data))
          if (data.length > 0) {
            data.forEach(email => {
              this.emails.push({
                id: email.id,
                from: email.from,
                to: email.to,
                subject: email.subject,
                body: email.body,
                date: email.dateTime,
                priority: email.priority,
                attachments: email.attachments
              })
            });
          } else {
            this.emails = [];
          }
          console.log(this.emails)
        })
        .catch(error => {
          console.error('Error:', error)
        });
      this.multiselect = false;
    },
    openContacts() {
      this.listing = 'Contacts';
      this.currentFolder = 'Contacts';
      this.closeCompose();
      fetch('http://localhost:8080/contact/get/' + this.useremail, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
      })
        .then(res => res.json())
        .then(data => {
          console.log(JSON.stringify(data))
          if (data.length > 0) {
            this.Contacts = [];
            data.forEach(contact => {
              this.Contacts.push({
                name: contact.name,
                emails: contact.emailAddresses
              })
            });
          } else {
            this.Contacts = [];
          }
        })
    },
    toggleMultiSelect() {
      console.log(this.selectedEmails);
      this.multiselect = !this.multiselect;
      this.selectedEmails = [];
    },
    compose() {
      this.composing = true;
      this.email = {
        id: '',
        from: this.useremail,
        to: '',
        subject: '',
        body: '',
        date: '',
        priority: '',
        attachments: []
      };
    },
    closeCompose() {
      this.composing = false;
      this.windowState = 'viewFolders';
    },
    openDraft(id) {
      this.windowState = 'viewDraft';
      this.email = this.emails.find(email => email.id === id);
      console.log(this.email);
    },
    openMail(id) {
      this.windowState = 'viewMail';
      this.email = this.emails.find(email => email.id === id);
    },
    sort(option, order) {
      if(this.currentFolder === 'Contacts'){
        fetch(`http://localhost:8080/contact/sort/` + this.useremail + '/' + order, {
          method: 'GET',
          headers: { 'Content-Type': 'application/json' },
        })
          .then(res => res.json())
          .then(data => {
            this.Contacts = [];
            console.log(data);
            data.forEach(contact => {
              this.Contacts.push({
                name: contact.name,
                emails: contact.emailAddresses
              })
            });
          })
          .catch(error => {
            console.error('Error:', error)
          });
      }else{
        fetch(`http://localhost:8080/${this.useremail}/${this.currentFolder}?sortingCriteria=${option}&sortingOrder=${order}`, {
          method: 'GET',
          headers: { 'Content-Type': 'application/json' },
        })
          .then(res => res.json())
          .then(data => {
            console.log(JSON.stringify(data))
            if (data.length > 0) {
              this.emails = [];
              data.forEach(email => {
                this.emails.push({
                  id: email.id,
                  from: email.from,
                  to: email.to,
                  subject: email.subject,
                  body: email.body,
                  date: email.dateTime,
                  priority: email.priority,
                  attachments: email.attachments
                })
              });
            }
            console.log(this.emails)
          })
          .catch(error => {
            console.error('Error:', error)
          });
      }
    },
    deleteEmail(selEmails) {
      console.log('selected emails:');
      console.log(this.selectedEmails);
      for (let email of selEmails) {
        fetch('http://localhost:8080/' + this.useremail + '/' + this.currentFolder.toLowerCase() + '/' + email.id, {
          method: 'DELETE',
          headers: { 'Content-Type': 'application/json' },
        }).then(res => res.json())
          .then(data => {
            console.log(JSON.stringify(data))
            this.emails = this.emails.filter(e => e.id !== email.id);
          })
      }
      this.selectedEmails = [];
    },
    AddContact() {
      let contact = {
        name: prompt("Please enter contact name", "New Contact"),
        emailAddress: prompt("Please enter email", "NewGuest@CSE.com")
      };
      fetch('http://localhost:8080/contact/create/' + this.useremail + '/' + contact.name + '/' + contact.emailAddress,{
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
      }).then(res => res.text())
        .then(data => {
          if(data === 'Contact added successfully'){
            console.log(data);
            this.openContacts();
          }else if(data === 'Another Contact Has This Email'){
            alert(data);
          }else if(data === 'No Such User Found'){
            alert('Invalid email address');
          }
        })
      this.openContacts();
    },
    RenameContact(contact) {
      let newName = prompt("Please enter new contact name", contact.name);
      fetch('http://localhost:8080/contact/rename/' + this.useremail + '/' + newName + '/' + contact.emails[0], {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
      }).then(res => res.text())
        .then(data => {
          this.openContacts();
          console.log(data);
        })
    },
    DeleteContact(contact) {
      fetch('http://localhost:8080/contact/delete/' + this.useremail + '/' + contact.emails[0], {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
      }).then(res => res.text())
        .then(data => {
          this.openContacts();
          console.log(data);
        })
    },
    AddEmail(contact) {
      let email = prompt("Please enter email", "NewEmail@cse.com");
      fetch('http://localhost:8080/contact/add/' + this.useremail + '/' + contact.emails[0] + '/' + email, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
      }).then(res => res.text())
        .then(data => {
          if(data === 'Contact email successfully added'){
            this.openContacts();
          }else if(data === 'Another Contact Has This Email'){
            alert(data);
          }else if(data === 'No Such User Found'){
            alert('Invalid email address');
          }
        })
    },
    RenameContactEmail(email) {
      let newName = prompt("Please enter new email", email);
      fetch('http://localhost:8080/contact/reset/' + this.useremail + '/' + email + '/' + newName,{
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
      }).then(res => res.text())
        .then(data => {
          if(data === 'Contact email successfully reset'){
            this.openContacts();
          }else if(data === 'Another Contact Has This Email'){
            alert(data);
          }else if(data === 'No Such User Found'){
            alert('Invalid email address');
          }
        })
    },
    DeleteContactEmail(contact, email) {
      fetch('http://localhost:8080/contact/remove/' + this.useremail + '/' + contact.emails[0] + '/' + email, {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
      }).then(res => res.text())
        .then(data => {
          this.openContacts();
          console.log(data);
        })
    },
    search(filterValue, filterCriteria) {
      if(this.currentFolder === 'Contacts'){
        if(filterValue === ''){
          this.openContacts();
          return;
        }
        fetch(`http://localhost:8080/contact/search/` + this.useremail + '/' + filterValue, {
          method: 'GET',
          headers: { 'Content-Type': 'application/json' },
        })
          .then(res => res.json())
          .then(data => {
            this.Contacts = [];
            console.log(data);
            data.forEach(contact => {
              this.Contacts.push({
                name: contact.name,
                emails: contact.emailAddresses
              })
            });
          })
          .catch(error => {
            console.error('Error:', error)
          });
      }else{
        filterCriteria = filterCriteria.join(',')
        console.log(filterValue, filterCriteria);
        fetch(`http://localhost:8080/${this.useremail}/${this.currentFolder}/filter?filterCriteria=${filterCriteria}&filterValue=${filterValue}`, {
          method: 'GET',
          headers: { 'Content-Type': 'application/json' },
        })
          .then(res => res.json())
          .then(data => {
            this.emails = [];
            data.forEach(email => {
              this.emails.push({
                id: email.id,
                from: email.from,
                to: email.to,
                subject: email.subject,
                body: email.body,
                date: email.dateTime,
                priority: email.priority,
                attachments: email.attachments
              })
            });
            console.log(this.emails)
          })
          .catch(error => {
            console.error('Error:', error)
          });
      }
    },
    moveEmail(selEmails) {
      let folder = prompt("Please enter folder name", "New Folder");
      if(folder == null) return;
      if(this.Folders.find(f => f.name === folder) == null){
        alert('NO such Folder')
        return;
      }
      this.getUsername(this.useremail)
      this.addedFolder = folder
      console.log(selEmails);
      for (let mail of selEmails) {
        if (mail === null) continue
        fetch(`http://localhost:8080/${this.useremail}/${this.currentFolder}/${folder}/${mail.id}`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
        }).then(res => res.json())
          .then(data => {
            console.log(data)
          })
      }
      this.selectedEmails = [];
    }
  },
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  width: 100%;
}

body {
  margin: 0;
  padding-top: 0%;
  height: 100%;
  width: 100%;
}

footer {
  background-color: rgba(3, 4, 5, 0.763);
  height: 5vh;
  color: whitesmoke;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 2vh;
}

.Title {
  font-size: 3.1vh;
  font-weight: bold;
  color: white;
  background-color: rgba(3, 4, 5, 0.763);
  height: fit-content;
  padding-bottom: 0.2em;
  width: 100vw;
  margin: 0;
  display: flex;
}

.logout {
  background-color: transparent;
  border: none;
  color: white;
  font-size: 2vh;
  cursor: pointer;
}

.logout:hover {
  color: black;
}
</style>
