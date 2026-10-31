

const API_BASE = '/api';

// Helper: Show Alert
function showAlert(message, type = 'success') {
    const alertBox = document.getElementById('alertBox');
    if (!alertBox) return;

    alertBox.className = `alert alert-${type}`;
    alertBox.innerHTML = `
        <span>${message}</span>
        <button type="button" class="alert-close" onclick="closeAlert()">&times;</button>
    `;
    alertBox.style.display = 'flex';

    setTimeout(() => {
        if (alertBox.style.display !== 'none') {
            closeAlert();
        }
    }, 6000);
}

function closeAlert() {
    const alertBox = document.getElementById('alertBox');
    if (alertBox) alertBox.style.display = 'none';
}

// Generic API caller with error parsing
async function apiCall(endpoint, method = 'GET', data = null) {
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    };
    if (data && (method === 'POST' || method === 'PUT')) {
        options.body = JSON.stringify(data);
    }

    try {
        const response = await fetch(`${API_BASE}${endpoint}`, options);
        if (response.status === 204) {
            return null;
        }

        const json = await response.json();
        if (!response.ok) {
            let errorMsg = json.message || 'An error occurred during request.';
            if (json.validationErrors) {
                const details = Object.entries(json.validationErrors)
                    .map(([field, msg]) => `${field}: ${msg}`)
                    .join('; ');
                errorMsg += ` (${details})`;
            }
            throw new Error(errorMsg);
        }
        return json;
    } catch (err) {
        console.error('API Error:', err);
        throw err;
    }
}

// ==========================================
// 1. DASHBOARD
// ==========================================
async function loadDashboard() {
    try {
        const summary = await apiCall('/dashboard/summary');

        // Update stat counters
        const totalCampsEl = document.getElementById('statTotalCamps');
        const totalFamiliesEl = document.getElementById('statTotalFamilies');
        const totalPersonsEl = document.getElementById('statTotalPersons');
        const totalStockEl = document.getElementById('statTotalStock');
        const totalDistEl = document.getElementById('statTotalDistributions');
        const lowStockEl = document.getElementById('statLowStock');

        if (totalCampsEl) totalCampsEl.textContent = summary.totalCamps || 0;
        if (totalFamiliesEl) totalFamiliesEl.textContent = summary.totalFamilies || 0;
        if (totalPersonsEl) totalPersonsEl.textContent = summary.totalPersonsAccommodated || 0;
        if (totalStockEl) totalStockEl.textContent = summary.totalStockQuantity || 0;
        if (totalDistEl) totalDistEl.textContent = summary.totalDistributions || 0;
        if (lowStockEl) lowStockEl.textContent = summary.lowStockCount || 0;

        // Populate recent camps
        const campsBody = document.getElementById('recentCampsBody');
        if (campsBody) {
            if (!summary.recentCamps || summary.recentCamps.length === 0) {
                campsBody.innerHTML = '<tr><td colspan="5" class="empty-state">No records available</td></tr>';
            } else {
                campsBody.innerHTML = summary.recentCamps.map(c => `
                    <tr>
                        <td><strong>#${c.id}</strong></td>
                        <td>${c.name}</td>
                        <td>${c.location}</td>
                        <td>${c.currentOccupancy} / ${c.capacity}</td>
                        <td><span class="badge ${c.status === 'ACTIVE' ? 'badge-success' : 'badge-danger'}">${c.status}</span></td>
                    </tr>
                `).join('');
            }
        }

        // Populate recent families
        const familiesBody = document.getElementById('recentFamiliesBody');
        if (familiesBody) {
            if (!summary.recentFamilies || summary.recentFamilies.length === 0) {
                familiesBody.innerHTML = '<tr><td colspan="5" class="empty-state">No records available</td></tr>';
            } else {
                familiesBody.innerHTML = summary.recentFamilies.map(f => `
                    <tr>
                        <td><strong>#${f.id}</strong></td>
                        <td>${f.familyHead}</td>
                        <td>${f.memberCount} members</td>
                        <td>${f.campName || ('Camp #' + f.campId)}</td>
                        <td><span class="badge badge-success">${f.status}</span></td>
                    </tr>
                `).join('');
            }
        }

        // Populate recent distributions
        const distBody = document.getElementById('recentDistBody');
        if (distBody) {
            if (!summary.recentDistributions || summary.recentDistributions.length === 0) {
                distBody.innerHTML = '<tr><td colspan="6" class="empty-state">No records available</td></tr>';
            } else {
                distBody.innerHTML = summary.recentDistributions.map(d => `
                    <tr>
                        <td><strong>#${d.id}</strong></td>
                        <td>${d.familyName || ('Family #' + d.familyId)}</td>
                        <td>${d.supplyName} (${d.quantity} ${d.unit})</td>
                        <td>${d.campName || ('Camp #' + d.campId)}</td>
                        <td>${d.distributionDate}</td>
                        <td><span class="badge badge-success">${d.status}</span></td>
                    </tr>
                `).join('');
            }
        }

    } catch (err) {
        showAlert('Failed to load dashboard metrics: ' + err.message, 'danger');
    }
}

// ==========================================
// 2. CAMP MANAGEMENT
// ==========================================
async function loadCamps() {
    try {
        const camps = await apiCall('/camps');
        const tbody = document.getElementById('campsTableBody');
        if (!tbody) return;

        if (!camps || camps.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" class="empty-state">No records available</td></tr>';
            return;
        }

        tbody.innerHTML = camps.map(camp => {
            const occupancyPct = camp.capacity > 0 ? Math.round((camp.currentOccupancy / camp.capacity) * 100) : 0;
            const badgeClass = camp.status === 'ACTIVE' ? 'badge-success' : 'badge-danger';
            return `
                <tr>
                    <td><strong>#${camp.id}</strong></td>
                    <td>${camp.name}</td>
                    <td>${camp.location}</td>
                    <td>${camp.capacity} persons</td>
                    <td>${camp.currentOccupancy} (${occupancyPct}%)</td>
                    <td><span class="badge ${badgeClass}">${camp.status}</span></td>
                    <td class="table-actions">
                        <button class="btn btn-secondary btn-sm" onclick="editCamp(${camp.id}, '${escapeHtml(camp.name)}', '${escapeHtml(camp.location)}', ${camp.capacity}, '${camp.status}')">Edit</button>
                        <button class="btn btn-danger btn-sm" onclick="deleteCamp(${camp.id})">Delete</button>
                    </td>
                </tr>
            `;
        }).join('');
    } catch (err) {
        showAlert('Error loading camps: ' + err.message, 'danger');
    }
}

async function handleCampSubmit(event) {
    event.preventDefault();
    const id = document.getElementById('campId').value;
    const name = document.getElementById('campName').value.trim();
    const location = document.getElementById('campLocation').value.trim();
    const capacity = parseInt(document.getElementById('campCapacity').value, 10);
    const status = document.getElementById('campStatus').value;

    const payload = { name, location, capacity, status };

    try {
        if (id) {
            await apiCall(`/camps/${id}`, 'PUT', payload);
            showAlert(`Camp "${name}" updated successfully!`, 'success');
        } else {
            await apiCall('/camps', 'POST', payload);
            showAlert(`New camp "${name}" created successfully!`, 'success');
        }
        resetCampForm();
        loadCamps();
    } catch (err) {
        showAlert(err.message, 'danger');
    }
}

function editCamp(id, name, location, capacity, status) {
    document.getElementById('campId').value = id;
    document.getElementById('campName').value = name;
    document.getElementById('campLocation').value = location;
    document.getElementById('campCapacity').value = capacity;
    document.getElementById('campStatus').value = status;

    document.getElementById('campSubmitBtn').textContent = 'Update Camp';
    document.getElementById('campFormTitle').textContent = `Edit Camp #${id}`;
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

function resetCampForm() {
    const form = document.getElementById('campForm');
    if (form) form.reset();
    document.getElementById('campId').value = '';
    document.getElementById('campSubmitBtn').textContent = 'Save Camp';
    document.getElementById('campFormTitle').textContent = 'Add Relief Camp';
}

async function deleteCamp(id) {
    if (!confirm(`Are you sure you want to delete Camp #${id}? This will also delete related supplies and families.`)) {
        return;
    }
    try {
        await apiCall(`/camps/${id}`, 'DELETE');
        showAlert(`Camp #${id} deleted successfully!`, 'success');
        loadCamps();
    } catch (err) {
        showAlert('Cannot delete camp: ' + err.message, 'danger');
    }
}

// ==========================================
// 3. SUPPLY MANAGEMENT
// ==========================================
async function loadSupplies() {
    try {
        // Load camps for dropdown
        const camps = await apiCall('/camps');
        const campSelect = document.getElementById('supplyCampId');
        if (campSelect) {
            campSelect.innerHTML = '<option value="">-- Select Relief Camp --</option>' +
                camps.map(c => `<option value="${c.id}">${c.name} (${c.location})</option>`).join('');
        }

        const supplies = await apiCall('/supplies');
        const tbody = document.getElementById('suppliesTableBody');
        if (!tbody) return;

        if (!supplies || supplies.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" class="empty-state">No records available</td></tr>';
            return;
        }

        tbody.innerHTML = supplies.map(s => `
            <tr>
                <td><strong>#${s.id}</strong></td>
                <td>${s.name}</td>
                <td><span class="badge badge-secondary">${s.type}</span></td>
                <td><strong>${s.quantity}</strong></td>
                <td>${s.unit}</td>
                <td>${s.campName || ('Camp #' + s.campId)}</td>
                <td>${s.receivedDate}</td>
                <td class="table-actions">
                    <button class="btn btn-secondary btn-sm" onclick="editSupply(${s.id}, '${escapeHtml(s.name)}', '${escapeHtml(s.type)}', ${s.quantity}, '${escapeHtml(s.unit)}', ${s.campId}, '${s.receivedDate}')">Edit</button>
                    <button class="btn btn-danger btn-sm" onclick="deleteSupply(${s.id})">Delete</button>
                </td>
            </tr>
        `).join('');
    } catch (err) {
        showAlert('Error loading supplies: ' + err.message, 'danger');
    }
}

async function handleSupplySubmit(event) {
    event.preventDefault();
    const id = document.getElementById('supplyId').value;
    const name = document.getElementById('supplyName').value.trim();
    const type = document.getElementById('supplyType').value.trim();
    const quantity = parseInt(document.getElementById('supplyQuantity').value, 10);
    const unit = document.getElementById('supplyUnit').value.trim();
    const campId = parseInt(document.getElementById('supplyCampId').value, 10);
    const receivedDate = document.getElementById('supplyReceivedDate').value;

    const payload = { name, type, quantity, unit, campId, receivedDate };

    try {
        if (id) {
            await apiCall(`/supplies/${id}`, 'PUT', payload);
            showAlert(`Supply "${name}" updated successfully!`, 'success');
        } else {
            await apiCall('/supplies', 'POST', payload);
            showAlert(`Supply "${name}" registered into inventory!`, 'success');
        }
        resetSupplyForm();
        loadSupplies();
    } catch (err) {
        showAlert(err.message, 'danger');
    }
}

function editSupply(id, name, type, quantity, unit, campId, receivedDate) {
    document.getElementById('supplyId').value = id;
    document.getElementById('supplyName').value = name;
    document.getElementById('supplyType').value = type;
    document.getElementById('supplyQuantity').value = quantity;
    document.getElementById('supplyUnit').value = unit;
    document.getElementById('supplyCampId').value = campId;
    if (receivedDate) document.getElementById('supplyReceivedDate').value = receivedDate;

    document.getElementById('supplySubmitBtn').textContent = 'Update Supply';
    document.getElementById('supplyFormTitle').textContent = `Edit Supply #${id}`;
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

function resetSupplyForm() {
    const form = document.getElementById('supplyForm');
    if (form) form.reset();
    document.getElementById('supplyId').value = '';
    document.getElementById('supplySubmitBtn').textContent = 'Save Supply';
    document.getElementById('supplyFormTitle').textContent = 'Add Incoming Supply';
}

async function deleteSupply(id) {
    if (!confirm(`Are you sure you want to delete Supply #${id}?`)) return;
    try {
        await apiCall(`/supplies/${id}`, 'DELETE');
        showAlert(`Supply #${id} deleted from inventory!`, 'success');
        loadSupplies();
    } catch (err) {
        showAlert('Cannot delete supply: ' + err.message, 'danger');
    }
}

// ==========================================
// 4. FAMILY MANAGEMENT (Capacity Validation)
// ==========================================
async function loadFamilies() {
    try {
        const camps = await apiCall('/camps');
        const campSelect = document.getElementById('familyCampId');
        if (campSelect) {
            campSelect.innerHTML = '<option value="">-- Select Relief Camp --</option>' +
                camps.map(c => `<option value="${c.id}">${c.name} (Occupancy: ${c.currentOccupancy}/${c.capacity})</option>`).join('');
        }

        const families = await apiCall('/families');
        const tbody = document.getElementById('familiesTableBody');
        if (!tbody) return;

        if (!families || families.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" class="empty-state">No records available</td></tr>';
            return;
        }

        tbody.innerHTML = families.map(f => `
            <tr>
                <td><strong>#${f.id}</strong></td>
                <td>${f.familyHead}</td>
                <td>${f.memberCount}</td>
                <td>${f.phone || '-'}</td>
                <td>${f.campName || ('Camp #' + f.campId)}</td>
                <td>${f.checkInDate}</td>
                <td><span class="badge ${f.status === 'CHECKED_IN' ? 'badge-success' : 'badge-secondary'}">${f.status}</span></td>
                <td class="table-actions">
                    <button class="btn btn-secondary btn-sm" onclick="editFamily(${f.id}, '${escapeHtml(f.familyHead)}', ${f.memberCount}, '${escapeHtml(f.phone || '')}', ${f.campId}, '${f.checkInDate}', '${f.status}')">Edit</button>
                    <button class="btn btn-danger btn-sm" onclick="deleteFamily(${f.id})">Delete</button>
                </td>
            </tr>
        `).join('');
    } catch (err) {
        showAlert('Error loading families: ' + err.message, 'danger');
    }
}

async function handleFamilySubmit(event) {
    event.preventDefault();
    const id = document.getElementById('familyId').value;
    const familyHead = document.getElementById('familyHead').value.trim();
    const memberCount = parseInt(document.getElementById('memberCount').value, 10);
    const phone = document.getElementById('familyPhone').value.trim();
    const campId = parseInt(document.getElementById('familyCampId').value, 10);
    const checkInDate = document.getElementById('familyCheckInDate').value;
    const status = document.getElementById('familyStatus') ? document.getElementById('familyStatus').value : 'CHECKED_IN';

    const payload = { familyHead, memberCount, phone, campId, checkInDate, status };

    try {
        if (id) {
            await apiCall(`/families/${id}`, 'PUT', payload);
            showAlert(`Family "${familyHead}" updated successfully!`, 'success');
        } else {
            await apiCall('/families', 'POST', payload);
            showAlert(`Family "${familyHead}" checked in successfully!`, 'success');
        }
        resetFamilyForm();
        loadFamilies();
    } catch (err) {
        // Clear message if camp capacity exceeded
        showAlert(err.message, 'danger');
    }
}

function editFamily(id, head, count, phone, campId, checkInDate, status) {
    document.getElementById('familyId').value = id;
    document.getElementById('familyHead').value = head;
    document.getElementById('memberCount').value = count;
    document.getElementById('familyPhone').value = phone;
    document.getElementById('familyCampId').value = campId;
    if (checkInDate) document.getElementById('familyCheckInDate').value = checkInDate;
    if (document.getElementById('familyStatus')) document.getElementById('familyStatus').value = status;

    document.getElementById('familySubmitBtn').textContent = 'Update Family';
    document.getElementById('familyFormTitle').textContent = `Edit Family #${id}`;
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

function resetFamilyForm() {
    const form = document.getElementById('familyForm');
    if (form) form.reset();
    document.getElementById('familyId').value = '';
    document.getElementById('familySubmitBtn').textContent = 'Register Family';
    document.getElementById('familyFormTitle').textContent = 'Register Affected Family';
}

async function deleteFamily(id) {
    if (!confirm(`Are you sure you want to remove Family #${id}? Camp occupancy will be freed.`)) return;
    try {
        await apiCall(`/families/${id}`, 'DELETE');
        showAlert(`Family #${id} discharged/removed successfully!`, 'success');
        loadFamilies();
    } catch (err) {
        showAlert('Cannot delete family: ' + err.message, 'danger');
    }
}

// ==========================================
// 5. INVENTORY MANAGEMENT
// ==========================================
async function loadInventory() {
    try {
        const items = await apiCall('/inventory');
        const tbody = document.getElementById('inventoryTableBody');
        if (!tbody) return;

        if (!items || items.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" class="empty-state">No records available</td></tr>';
            return;
        }

        tbody.innerHTML = items.map(item => {
            let badgeClass = 'badge-success';
            let label = 'AVAILABLE';
            if (item.availableQuantity <= 0) {
                badgeClass = 'badge-danger';
                label = 'OUT OF STOCK';
            } else if (item.availableQuantity < 50) {
                badgeClass = 'badge-warning';
                label = 'LOW STOCK';
            }

            return `
                <tr>
                    <td><strong>#${item.supplyId}</strong></td>
                    <td>${item.supplyName}</td>
                    <td><span class="badge badge-secondary">${item.type}</span></td>
                    <td><strong style="font-size: 1.05rem;">${item.availableQuantity}</strong></td>
                    <td>${item.unit}</td>
                    <td>${item.campName || ('Camp #' + item.campId)}</td>
                    <td><span class="badge ${badgeClass}">${label}</span></td>
                </tr>
            `;
        }).join('');
    } catch (err) {
        showAlert('Error loading inventory: ' + err.message, 'danger');
    }
}

// ==========================================
// 6. DISTRIBUTION MANAGEMENT (Inventory Check)
// ==========================================
let allSuppliesCache = [];

async function loadDistributionsPage() {
    try {
        const camps = await apiCall('/camps');
        const campSelect = document.getElementById('distCampId');
        if (campSelect) {
            campSelect.innerHTML = '<option value="">-- Select Relief Camp --</option>' +
                camps.map(c => `<option value="${c.id}">${c.name}</option>`).join('');
        }

        allSuppliesCache = await apiCall('/supplies');
        await loadDistributionsTable();
    } catch (err) {
        showAlert('Error loading distribution form data: ' + err.message, 'danger');
    }
}

async function onCampSelectedForDistribution() {
    const campId = document.getElementById('distCampId').value;
    const familySelect = document.getElementById('distFamilyId');
    const supplySelect = document.getElementById('distSupplyId');

    familySelect.innerHTML = '<option value="">-- Select Family --</option>';
    supplySelect.innerHTML = '<option value="">-- Select Supply Item --</option>';

    if (!campId) return;

    try {
        const families = await apiCall(`/families/camp/${campId}`);
        familySelect.innerHTML = '<option value="">-- Select Family --</option>' +
            families.map(f => `<option value="${f.id}">${f.familyHead} (${f.memberCount} members)</option>`).join('');

        const supplies = allSuppliesCache.filter(s => s.campId == campId);
        supplySelect.innerHTML = '<option value="">-- Select Supply Item --</option>' +
            supplies.map(s => `<option value="${s.id}">${s.name} (Stock: ${s.quantity} ${s.unit})</option>`).join('');
    } catch (err) {
        showAlert('Error updating families or supplies for camp: ' + err.message, 'danger');
    }
}

async function handleDistributionSubmit(event) {
    event.preventDefault();
    const campId = parseInt(document.getElementById('distCampId').value, 10);
    const familyId = parseInt(document.getElementById('distFamilyId').value, 10);
    const supplyId = parseInt(document.getElementById('distSupplyId').value, 10);
    const quantity = parseInt(document.getElementById('distQuantity').value, 10);
    const remarks = document.getElementById('distRemarks').value.trim();

    const payload = { campId, familyId, supplyId, quantity, remarks };

    try {
        const res = await apiCall('/distributions', 'POST', payload);
        showAlert(`Distribution of ${quantity} items completed successfully! Inventory has been automatically updated.`, 'success');
        document.getElementById('distributionForm').reset();
        allSuppliesCache = await apiCall('/supplies');
        onCampSelectedForDistribution();
        loadDistributionsTable();
    } catch (err) {
        // Clear message if inventory insufficient
        showAlert(err.message, 'danger');
    }
}

async function loadDistributionsTable() {
    try {
        const dists = await apiCall('/distributions');
        const tbody = document.getElementById('distributionsTableBody');
        if (!tbody) return;

        if (!dists || dists.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" class="empty-state">No records available</td></tr>';
            return;
        }

        tbody.innerHTML = dists.map(d => `
            <tr>
                <td><strong>#${d.id}</strong></td>
                <td>${d.campName || ('Camp #' + d.campId)}</td>
                <td>${d.familyName || ('Family #' + d.familyId)}</td>
                <td>${d.supplyName}</td>
                <td><strong>${d.quantity}</strong> ${d.unit}</td>
                <td>${d.distributionDate}</td>
                <td>${d.remarks || '-'}</td>
                <td class="table-actions">
                    <button class="btn btn-danger btn-sm" onclick="rollbackDistribution(${d.id})">Rollback</button>
                </td>
            </tr>
        `).join('');
    } catch (err) {
        showAlert('Error loading distributions list: ' + err.message, 'danger');
    }
}

async function rollbackDistribution(id) {
    if (!confirm(`Are you sure you want to rollback Distribution #${id}? The distributed quantity will be restored back to inventory.`)) return;
    try {
        await apiCall(`/distributions/${id}`, 'DELETE');
        showAlert(`Distribution #${id} rolled back and stock restored!`, 'success');
        allSuppliesCache = await apiCall('/supplies');
        onCampSelectedForDistribution();
        loadDistributionsTable();
    } catch (err) {
        showAlert('Cannot rollback distribution: ' + err.message, 'danger');
    }
}

// ==========================================
// 7. REPORTS
// ==========================================
async function loadReports() {
    try {
        const summary = await apiCall('/dashboard/summary');
        const camps = await apiCall('/camps');
        const inventory = await apiCall('/inventory');
        const dists = await apiCall('/distributions');

        const reportStatsEl = document.getElementById('reportStats');
        if (reportStatsEl) {
            reportStatsEl.innerHTML = `
                <div class="stat-card">
                    <div class="stat-title">Active Camps</div>
                    <div class="stat-value">${summary.totalCamps}</div>
                    <div class="stat-desc">Operational relief centers</div>
                </div>
                <div class="stat-card green">
                    <div class="stat-title">Total Families</div>
                    <div class="stat-value">${summary.totalFamilies}</div>
                    <div class="stat-desc">${summary.totalPersonsAccommodated} total individuals sheltered</div>
                </div>
                <div class="stat-card amber">
                    <div class="stat-title">Stock Balance</div>
                    <div class="stat-value">${summary.totalStockQuantity}</div>
                    <div class="stat-desc">${summary.lowStockCount} items low in stock</div>
                </div>
                <div class="stat-card purple">
                    <div class="stat-title">Distributions</div>
                    <div class="stat-value">${summary.totalDistributions}</div>
                    <div class="stat-desc">Relief dispatch events</div>
                </div>
            `;
        }

        const campOverviewBody = document.getElementById('reportCampOverviewBody');
        if (campOverviewBody) {
            campOverviewBody.innerHTML = camps.map(c => {
                const pct = c.capacity > 0 ? Math.round((c.currentOccupancy / c.capacity) * 100) : 0;
                return `
                    <tr>
                        <td>#${c.id}</td>
                        <td><strong>${c.name}</strong></td>
                        <td>${c.location}</td>
                        <td>${c.capacity}</td>
                        <td>${c.currentOccupancy}</td>
                        <td>${pct}%</td>
                        <td><span class="badge ${c.status === 'ACTIVE' ? 'badge-success' : 'badge-danger'}">${c.status}</span></td>
                    </tr>
                `;
            }).join('');
        }
    } catch (err) {
        showAlert('Error loading report data: ' + err.message, 'danger');
    }
}

// Helper: Escape HTML string
function escapeHtml(text) {
    if (!text) return '';
    return text.toString()
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}
