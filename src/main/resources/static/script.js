let currentPage = 1;
const pageSize = 5;

function loadUsers(page = 1) {
    currentPage = page;
    fetch(`/api/users?page=${page}&size=${pageSize}`)
        .then(res => res.json())
        .then(data => {
            const userTable = document.getElementById("userTable");
            userTable.innerHTML = "";

            (data.list || []).forEach(user => {
                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td>${user.id}</td>
                    <td>${escapeHtml(user.name)}</td>
                    <td>${escapeHtml(user.email || '')}</td>
                    <td>
                        <button onclick="openPopup('edit', ${user.id}, '${user.name}', '${user.email || ''}')">编辑</button>
                        <button onclick="deleteUser(${user.id})">删除</button>
                    </td>
                `;
                userTable.appendChild(tr);
            });

            renderPagination(data);
        })
        .catch(err => console.error("加载用户失败:", err));
}

function renderPagination(pageInfo) {
    const pagination = document.getElementById("pagination");
    pagination.innerHTML = "";

    if (pageInfo.hasPreviousPage) {
        pagination.appendChild(createPageButton("上一页", pageInfo.prePage));
    }

    for (let i = 1; i <= pageInfo.pages; i++) {
        const btn = createPageButton(i, i);
        if (pageInfo.pageNum === i) btn.classList.add("active");
        pagination.appendChild(btn);
    }

    if (pageInfo.hasNextPage) {
        pagination.appendChild(createPageButton("下一页", pageInfo.nextPage));
    }
}

function createPageButton(text, page) {
    const btn = document.createElement("button");
    btn.textContent = text;
    btn.addEventListener("click", () => loadUsers(page));
    return btn;
}

function deleteUser(id) {
    if (confirm("确定要删除？")) {
        fetch(`/api/users/${id}`, { method: 'DELETE' })
            .then(() => loadUsers(currentPage))
            .catch(() => alert("删除失败"));
    }
}

function searchUser() {
    const name = document.getElementById("userName").value.trim();
    if (!name) {
        alert("请输入姓名进行检索");
        return;
    }
    console.log("查询用户:", name);
}

function openPopup(type, id = "", name = "", email = "") {
    document.getElementById("popupTitle").textContent = type === "edit" ? "编辑用户" : "追加用户";
    document.getElementById("popupUserName").value = name;
    document.getElementById("popupUserEmail").value = email;
    document.getElementById("popup").dataset.type = type;
    document.getElementById("popup").dataset.id = id;
    document.getElementById("popup").style.display = "block";
}

function closePopup() {
    document.getElementById("popup").style.display = "none";
}

function savePopupUser() {
    const type = document.getElementById("popup").dataset.type;
    const id = document.getElementById("popup").dataset.id;
    const name = document.getElementById("popupUserName").value.trim();
    const email = document.getElementById("popupUserEmail").value.trim();

    if (!name) {
        alert("姓名不能为空");
        return;
    }

    const user = { name, email };

    const url = type === "edit" ? `/api/users/${id}` : `/api/users`;
    const method = type === "edit" ? "PUT" : "POST";

    fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(user)
    }).then(() => {
        closePopup();
        loadUsers(currentPage);
    }).catch(() => alert("保存失败"));
}

function escapeHtml(text) {
    return text.replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#39;");
}

document.addEventListener("DOMContentLoaded", loadUsers);
