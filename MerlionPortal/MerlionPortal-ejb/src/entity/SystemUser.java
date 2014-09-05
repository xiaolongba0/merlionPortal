/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author MelodyPond_2
 */
@Entity
@Table(name = "systemuser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SystemUser.findAll", query = "SELECT s FROM SystemUser s"),
    @NamedQuery(name = "SystemUser.findBySystemUserId", query = "SELECT s FROM SystemUser s WHERE s.systemUserId = :systemUserId"),
    @NamedQuery(name = "SystemUser.findByFirstName", query = "SELECT s FROM SystemUser s WHERE s.firstName = :firstName"),
    @NamedQuery(name = "SystemUser.findByLastName", query = "SELECT s FROM SystemUser s WHERE s.lastName = :lastName"),
    @NamedQuery(name = "SystemUser.findByEmailAddress", query = "SELECT s FROM SystemUser s WHERE s.emailAddress = :emailAddress"),
    @NamedQuery(name = "SystemUser.findByPassword", query = "SELECT s FROM SystemUser s WHERE s.password = :password"),
    @NamedQuery(name = "SystemUser.findByPostalAddress", query = "SELECT s FROM SystemUser s WHERE s.postalAddress = :postalAddress"),
    @NamedQuery(name = "SystemUser.findByContactNumber", query = "SELECT s FROM SystemUser s WHERE s.contactNumber = :contactNumber"),
    @NamedQuery(name = "SystemUser.findBySalution", query = "SELECT s FROM SystemUser s WHERE s.salution = :salution"),
    @NamedQuery(name = "SystemUser.findByLocked", query = "SELECT s FROM SystemUser s WHERE s.locked = :locked"),
    @NamedQuery(name = "SystemUser.findByResetPasswordUponLogin", query = "SELECT s FROM SystemUser s WHERE s.resetPasswordUponLogin = :resetPasswordUponLogin"),
    @NamedQuery(name = "SystemUser.findByCreatedDate", query = "SELECT s FROM SystemUser s WHERE s.createdDate = :createdDate"),
    @NamedQuery(name = "SystemUser.findByUserType", query = "SELECT s FROM SystemUser s WHERE s.userType = :userType"),
    @NamedQuery(name = "SystemUser.findByActivated", query = "SELECT s FROM SystemUser s WHERE s.activated = :activated"),
    @NamedQuery(name = "SystemUser.findByTerminated", query = "SELECT s FROM SystemUser s WHERE s.terminated = :terminated"),
    @NamedQuery(name = "SystemUser.findByCredit", query = "SELECT s FROM SystemUser s WHERE s.credit = :credit")})
public class SystemUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "systemUserId")
    private Integer systemUserId;
    @Size(max = 45)
    @Column(name = "firstName")
    private String firstName;
    @Size(max = 45)
    @Column(name = "lastName")
    private String lastName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "emailAddress")
    private String emailAddress;
    @Size(max = 45)
    @Column(name = "password")
    private String password;
    @Size(max = 45)
    @Column(name = "postalAddress")
    private String postalAddress;
    @Size(max = 45)
    @Column(name = "contactNumber")
    private String contactNumber;
    @Size(max = 45)
    @Column(name = "salution")
    private String salution;
    @Column(name = "locked")
    private Boolean locked;
    @Column(name = "resetPasswordUponLogin")
    private Boolean resetPasswordUponLogin;
    @Column(name = "createdDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "userType")
    private String userType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activated")
    private boolean activated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "terminated")
    private boolean terminated;
    @Size(max = 45)
    @Column(name = "credit")
    private String credit;
    @JoinColumn(name = "UserRole_userRoleId", referencedColumnName = "userRoleId")
    @ManyToOne(optional = false)
    private UserRole userRoleuserRoleId;
    @JoinColumn(name = "Company_companyId", referencedColumnName = "companyId")
    @ManyToOne(optional = false)
    private Company companycompanyId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "systemUsersystemUserId")
    private List<SystemLog> systemLogList;

    public SystemUser() {
    }

    public SystemUser(Integer systemUserId) {
        this.systemUserId = systemUserId;
    }

    public SystemUser(Integer systemUserId, String emailAddress, String userType, boolean activated, boolean terminated) {
        this.systemUserId = systemUserId;
        this.emailAddress = emailAddress;
        this.userType = userType;
        this.activated = activated;
        this.terminated = terminated;
    }

    public Integer getSystemUserId() {
        return systemUserId;
    }

    public void setSystemUserId(Integer systemUserId) {
        this.systemUserId = systemUserId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getSalution() {
        return salution;
    }

    public void setSalution(String salution) {
        this.salution = salution;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getResetPasswordUponLogin() {
        return resetPasswordUponLogin;
    }

    public void setResetPasswordUponLogin(Boolean resetPasswordUponLogin) {
        this.resetPasswordUponLogin = resetPasswordUponLogin;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean getTerminated() {
        return terminated;
    }

    public void setTerminated(boolean terminated) {
        this.terminated = terminated;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public UserRole getUserRoleuserRoleId() {
        return userRoleuserRoleId;
    }

    public void setUserRoleuserRoleId(UserRole userRoleuserRoleId) {
        this.userRoleuserRoleId = userRoleuserRoleId;
    }

    public Company getCompanycompanyId() {
        return companycompanyId;
    }

    public void setCompanycompanyId(Company companycompanyId) {
        this.companycompanyId = companycompanyId;
    }

    @XmlTransient
    public List<SystemLog> getSystemLogList() {
        return systemLogList;
    }

    public void setSystemLogList(List<SystemLog> systemLogList) {
        this.systemLogList = systemLogList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (systemUserId != null ? systemUserId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SystemUser)) {
            return false;
        }
        SystemUser other = (SystemUser) object;
        if ((this.systemUserId == null && other.systemUserId != null) || (this.systemUserId != null && !this.systemUserId.equals(other.systemUserId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SystemUser[ systemUserId=" + systemUserId + " ]";
    }
    
}
