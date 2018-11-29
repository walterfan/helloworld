var UserList = Vue.extend({
    template: '#user-list',
    data() {
        return {
            searchKey: '',
            users: [],
            errors: []
        }
    },
    mounted () {
        axios
            .get('/api/v1/users')
            .then(response => {
                this.users= response.data;
            })
            .catch(e => {
                this.errors.push(e);
            });
    },
    methods: {
        searchUser: function() {
            axios.get('/api/v1/users/search?keyword=' + this.searchKey)
                .then(response => {
                    this.users= response.data;
                })
                .catch(e => {
                    this.errors.push(e)
                });
            router.push('/');
        }
    }
});

var AddUser = Vue.extend({
    template: '#add-user',
    data: function () {

        var rightNow = new Date();
        var later1 = rightNow.plusHours(1);
        var later2 = rightNow.plusHours(2);
        return {
            user: {
               username: '',
                password: '',
                email: '',
                phoneNumber: '1',
                roles: 'guest'
            },
            errors: []
        }
    },
    methods: {
        createUser: function() {
            console.log("--- createUser:" + this.user);
            axios.post('/api/v1/users',  this.user)
                .then(response => {})
                .catch(e => {
                    this.errors.push(e)
                })
            router.push('/');
        }
    }
});

var router = new VueRouter({routes:[
        { path: '/', component: UserList},
        { path: '/add-user', component: AddUser},
  
    ]});

var app = new Vue({
    router:router
}).$mount('#app')