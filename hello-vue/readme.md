
剖析一个实例, 代码如下


# Html



* note.html

```
<html lang="en">
<head>
  <meta charset="utf-8">
  <link rel='stylesheet prefetch' href='./css/bootstrap.min.css'>
  <link rel='stylesheet prefetch' href='./css/bootstrap-theme.min.css'>
  <link rel='stylesheet prefetch' href='./css/note.css'>
</head>
<body>

<!-- Html 中先声明一个容器, 分为标题 header, 内容 app-->
<div class="container">
  <header class="page-header">
    <div class="branding">
      
      <h1>Notebook</h1>
      
    </div>
  </header>
  <main id="app">
    <router-view></router-view>
  </main>
</div>

<!--  note list -->
<!-- 定义若干模板 -->
<template id="note-list">
  <div>
    
    <div class="filters row">
      <div class="form-group col-sm-4">
        <input v-model="searchKey" class="form-control" id="search-element"  placeholder="Title" requred/>
      </div>
      <div class="actions form-group col-sm-4">
      
      <!-- 注意这个 router-link, 把链接绑定到添加笔记的路径上 -->
      <router-link class="btn btn-default" v-bind:to="{path: '/add-note'}">
        <span class="glyphicon glyphicon-plus"></span>
        Add
      </router-link>
    </div>
    </div>
    <table class="table">
      <thead>
      <tr>
        <th>Title</th>
        <th>Content</th>
        <th>Tag</th>
        <th class="col-sm-2">Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="note in filteredNotes">
        <td>
        <!-- 注意这个 router-link, 把链接绑定到显示笔记的路径上了 -->
          <router-link v-bind:to="{path: '/note/' + note.id}">{{ note.title }}</router-link>
        </td>
        <td>{{ note.content }}</td>
        <td>
          {{ note.tag }}
        </td>
        <td>
         <!-- 注意这个 router-link, 把链接绑定到编辑笔记的路径上了 -->
          <router-link class="btn btn-warning btn-xs" v-bind:to="{path: '/note/'+ note.id +'/edit'}">Edit</router-link>
          <!-- 注意这个 router-link, 把链接绑定到删除笔记的路径上了 -->
          <router-link class="btn btn-danger btn-xs" v-bind:to="{path: '/note/'+ note.id +'/delete'}">Delete</router-link>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<!-- 创建笔记的模板 -->
<template id="add-note">
  <div>
    <h2>Add new note</h2>
    <form v-on:submit="createNote">
      <div class="form-group">
        <label for="add-title">Title</label>
        <input class="form-control" id="add-title" v-model="note.title" required/>
      </div>
      <div class="form-group">
        <label for="add-content">Content</label>
        <textarea class="form-control" id="add-content" rows="10" v-model="note.content"></textarea>
      </div>
      <div class="form-group">
        <label for="add-tag">Tag</label>
        <input type="text" class="form-control" id="add-tag" v-model="note.tag"/>
      </div>
      <button type="submit" class="btn btn-primary">Create</button>
      <router-link class="btn btn-default" v-bind:to="'/'">Cancel</router-link>
    </form>
  </div>
</template>

<!-- 显示笔记的模板 -->
<template id="note">
  <div>
    <h2>{{ note.title }}</h2>
    <b>Description: </b>
    <div>{{ note.content }}</div>
    <b>Price:</b>
    <div>{{ note.tag }}</div>
    <br/>
    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>
    <router-link v-bind:to="'/'">Back to note list</router-link>
  </div>
</template>

<!-- 编辑笔记的模板 -->
<template id="note-edit">
  <div>
    <h2>Edit note</h2>
    <form v-on:submit="updateNote">
      <div class="form-group">
        <label for="edit-title">Title</label>
        <input class="form-control" id="edit-title" v-model="note.title" required/>
      </div>
      <div class="form-group">
        <label for="edit-content">Content</label>
        <textarea class="form-control" id="edit-content" rows="3" v-model="note.content"></textarea>
      </div>
      <div class="form-group">
        <label for="edit-tag">Tag</label>
        <input type="text" class="form-control" id="edit-tag" v-model="note.tag"/>
      </div>
      <button type="submit" class="btn btn-primary">Save</button>
      <router-link class="btn btn-default" v-bind:to="'/'">Cancel</router-link>
    </form>
  </div>
</template>

<!-- 删除笔记的模板 -->

<template id="note-delete">
  <div>
    <h2>Delete note {{ note.title }} ?</h2>
    <form v-on:submit="deleteNote">
      <p>The action cannot be undone.</p>
      <button type="submit" class="btn btn-danger">Delete</button>
      <router-link class="btn btn-default" v-bind:to="'/'">Cancel</router-link>
    </form>
  </div>
</template>


<script src="./js/vue.js"></script>
<script src="./js/vue-router.js"></script>
<script src="./js/note.js"></script>
<script type="text/javascript">
  

</script>
</body>
</html>
```

## note.js

```
//定义供测试的预设的笔记数据
var notes = [
  {id: 1, title: 'Plan', content: 'what to do', tag: 'tip', createTime: "2018-1-1T07:10:10Z"},
  {id: 2, title: 'Do', content: 'do one thing one time', tag: 'tip', createTime: "2018-1-1T08:10:10Z"},
  {id: 3, title: 'Check', content: 'review and organize', tag: 'tip', createTime: "2018-1-1T09:10:10Z"},
  {id: 3, title: 'Action', content: 'adjust plan and do', tag: 'tip', createTime: "2018-1-1T10:10:10Z"}
];

//寻找笔记的函数, 从上述列表中寻找
function findNote (noteId) {
  return notes[findNoteKey(noteId)];
};

function findNoteKey (noteId) {
  for (var key = 0; key < notes.length; key++) {
    if (notes[key].id == noteId) {
      return key;
    }
  }
};

//笔记列表对象, 绑定模板到 note-list, 指定数据和 computed 函数
var List = Vue.extend({
  template: '#note-list',
  data: function () {
    return {notes: notes, searchKey: ''};
  },
  computed: {
    filteredNotes: function () {
      return this.notes.filter(function (note) {
        return this.searchKey=='' || note.title.indexOf(this.searchKey) !== -1;
      },this);
    }
  }
});

//笔记对象

var Note = Vue.extend({
  template: '#note',
  data: function () {
    return {note: findNote(this.$route.params.note_id)};
  }
});

//笔记编辑对象, 包括一个笔记修改方法 updateNote
var NoteEdit = Vue.extend({
  template: '#note-edit',
  data: function () {
    return {note: findNote(this.$route.params.note_id)};
  },
  methods: {
    updateNote: function () {
      var note = this.note;
      notes[findNoteKey(note.id)] = {
        id: note.id,
        title: note.title,
        content: note.content,
        tag: note.tag
      };
      router.push('/');
    }
  }
});

//笔记删除对象, 包含一个删除方法 deleteNote
var NoteDelete = Vue.extend({
  template: '#note-delete',
  data: function () {
    return {note: findNote(this.$route.params.note_id)};
  },
  methods: {
    deleteNote: function () {
      notes.splice(findNoteKey(this.$route.params.note_id), 1);
      router.push('/');
    }
  }
});

//添加笔记对象, 包含一个创建笔记的方法 createNote

var AddNote = Vue.extend({
  template: '#add-note',
  data: function () {
    return {note: {title: '', content: '', tag: ''}}
  },
  methods: {
    createNote: function() {
      var note = this.note;
      notes.push({
        id: Math.random().toString().split('.')[1],
        title: note.title,
        content: note.content,
        tag: note.tag
      });
      router.push('/');
    }
  }
});

//重点来了, 这里定义的路由表, List 以及 CRUD
var router = new VueRouter({routes:[
  { path: '/', component: List},
  { path: '/note/:note_id', component: Note, title: 'note'},
  { path: '/add-note', component: AddNote},
  { path: '/note/:note_id/edit', component: NoteEdit, title: 'note-edit'},
  { path: '/note/:note_id/delete', component: NoteDelete, title: 'note-delete'}
]});

var app = new Vue({
  router:router
}).$mount('#app')
```