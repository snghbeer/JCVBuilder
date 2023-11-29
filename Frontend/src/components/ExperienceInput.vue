<script setup>
import { ref, toRefs } from 'vue';

const props = defineProps({
 experience: Object,
});

const { startMonthYear, endMonthYear, jobDescription } = toRefs(props.experience);

</script>

<template>
    <div class="experience-input">
        <div>
            <div style="display: flex; justify-content: space-between; width: 50%;">
                <div><label class="periodLabel" for="startMonthYear">Start Month-Year</label>
                <input type="month" id="startMonthYear" v-model="startMonthYear" required /></div>
 
                <div><label class="periodLabel" for="endMonthYear">End Month-Year</label>
                <input type="month" id="endMonthYear" v-model="endMonthYear" /></div>
            </div>
            <div>
                <label for="jobDescription">Job Description</label>
                <div>
                   <textarea style="resize: none; width: 100%;" id="jobDescription" v-model="jobDescription" required></textarea>
                </div>
            </div>
        </div>
    </div>
 </template>
  

  
<style scoped>
.experience-input {
    margin-bottom: 20px;
}

.periodLabel{
    padding-right: 0.5rem;
}

</style>