<template>
    <div class="welcomepage">
        <div class="regBox">
            <i class="Welcome">Welcome</i>
            <form @submit.prevent>
                <div class="form-group">
                    <input type="email" id="email" v-model="email" placeholder="Email" />
                </div>

                <div v-if = "this.page === 'register' " class="form-group">
                  <input type="text" id="username" v-model="username" placeholder="Username" />
                </div>

                <div class="form-group">
                  <input type="password" id="password" v-model="password" placeholder="password" />
                </div>

                <div class="welmsg" :class="msgColor()">
                    {{ WelcomeMsg }}
                </div>
                <div class="Buttons">
                    <button v-if = "this.page === 'login'" class="Btn" @click="this.$emit('login',this.email, this.password);">Login</button>
                    <button v-if="this.page === 'register'" class="Btn" @click="this.$emit('signUp', this.username, this.email,this.password)">SignUp</button>
                </div>
              <p v-if="this.page === 'login'" >Don't have an account?<button @click = "pageTransition('register')" class = "switch-button">Register</button>
              </p>
              <p v-if="this.page === 'register'" >Already have an account?<button @click = "pageTransition('login')" class = "switch-button">login</button>
              </p>
            </form>
        </div>
    </div>
</template>

<script>
export default {
    name: 'WelcomePage',
    props: ['WelcomeMsg'],
    data() {
        return {
            username: '',
            email: '',
            page :'login',
            password: '',
        };
    },
    methods: {
        msgColor() {
            if (this.WelcomeMsg === 'Invalid email or password!') {
                return 'red';
            } else if (this.WelcomeMsg === 'User already exists!') {
                return 'red';
            } else if (this.WelcomeMsg === 'User created successfully!') {
                return 'green';
            }
        },
        pageTransition(page){
          this.page = page
          this.username = ''
          this.email = ''
          this.password = ''
         this.$emit('updateMessage')
        }
    },
};
</script>

<style scoped>
.welcomepage {
    background-color: rgb(164, 211, 222);
    height: 100vh;
    width: 100vw;
    display: flex;
    justify-content: center;
    align-items: center;
}
.Welcome{
    position: relative;
    top: -5%;
    font-size: 4vh;
    font-weight: bold;
}
.regBox {
    border: 1px solid black;
    padding: 5vh;
    width: 30vw;
    min-height: 50px;
    overflow: hidden;
    background-color: white;
}
.form-group {
    height: 7vh;
}
#username, #email, #password{
    position: relative;
    left: -1%;
    width: 25vw;
    height: 4vh;
    border: none;
    background-color: transparent;
    border-bottom: 1px solid black;
    padding: 1vh;
    margin: 1vh;
}
#username:focus, #email:focus, #password:focus{
    outline: none;
}
.welmsg{
    padding: 1vh;
    width: 100%;
    height: 4vh;
    font-size: 1.7vh;
    font-weight: bold;
    text-align: center;
}
.red{
    color: red;
}
.green{
    color: green;
}
.Buttons{
    display: flex;
    justify-content: space-around;
    align-items: center;
    width: 100%;
    height: 7vh;
}
.Btn{
    width: 12vw;
    height: 5vh;
    padding: 1vh;
    border-radius: 20px;
    cursor: pointer;
    font-weight: bold;
    background-color: azure;
}
.Btn:hover{
    background-color: rgb(205, 246, 255);
}
.switch-button{
    border: none;
    background-color: transparent;
    cursor: pointer;
    font-weight: bold;
    color: blue;
    font-size: 15px;
    text-decoration: underline;
}
</style>
