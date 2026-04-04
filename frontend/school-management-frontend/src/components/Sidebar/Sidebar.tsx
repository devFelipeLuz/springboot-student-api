import {
  FaUserGraduate,
  FaChalkboardTeacher,
  FaSchool,
  FaBook
} from "react-icons/fa";

import {
  SidebarContainer,
  SidebarItem
} from "./styles";

function Sidebar() {
  return (
    <SidebarContainer>
      <SidebarItem>
        <FaUserGraduate />
        <span>Students</span>
      </SidebarItem>

      <SidebarItem>
        <FaChalkboardTeacher />
        <span>Professors</span>
      </SidebarItem>

      <SidebarItem>
        <FaSchool />
        <span>Classrooms</span>
      </SidebarItem>

      <SidebarItem>
        <FaBook />
        <span>Subjects</span>
      </SidebarItem>
    </SidebarContainer>
  );
}

export default Sidebar;