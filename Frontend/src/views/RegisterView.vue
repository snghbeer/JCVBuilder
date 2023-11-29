<script setup>
import { ref } from 'vue';
import router from '../router/index'
import { host } from '../config';

const username = ref('');
const password = ref('');
const cpassword = ref('');

const signup = async () => {
    if(password.value !== cpassword.value) return alert("Passwords don't match!")
    const response = await fetch(host + "/register", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username: username.value,
            password: password.value,
        })
    })
    const result = await response.text()
    if (response.ok) {
        router.push('/login')
    } else {
        alert(result)
    }
};
</script>

<template>
    <main style="">
      <div
        style="background-color: #fff;  display: flex; flex-direction: column; align-items: center; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);">
        <form @submit.prevent="signup" id="credentials">
          <div style="text-align: left; max-width: 300px; margin: 0 auto;">
            <div>
              <label class="loginLabel" for="username" style="width: 100px; display: inline-block;">Username</label>
              <input v-model="username" type="text" name="username" id="username"
                style="padding: 8px; margin: 8px; border: 1px solid #ccc; border-radius: 4px;">
            </div>
            <div>
              <label class="loginLabel" for="password" style="width: 100px; display: inline-block;">Password</label>
              <input v-model="password" type="password" name="password" id="password"
                style="padding: 8px; margin: 8px; border: 1px solid #ccc; border-radius: 4px;">
            </div>
            <div>
              <label class="loginLabel" for="password" style="width: 100px; display: inline-block;">Confirm password</label>
              <input v-model="cpassword" type="password" name="cpassword" id="cpassword"
                style="padding: 8px; margin: 8px; border: 1px solid #ccc; border-radius: 4px;">
            </div>
            <div style="display: flex; justify-content: center; padding-top: 20px;">
                <button type="submit" >Sign up</button>
            </div>
          </div>
        </form>
      </div>
    </main>
  </template>
  
  

<style scoped>
.loginLabel {
    color: black;
}
</style>