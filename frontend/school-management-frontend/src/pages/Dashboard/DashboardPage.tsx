import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import Header from "../../components/Header/Header";
import Sidebar from "../../components/Sidebar/Sidebar";
import CountCard from "../../components/Card/CountCard/CountCard";

import {
  DashboardWrapper,
  MainLayout,
  Content,
  CardsGrid
} from "./styles";

function DashboardPage() {
  const navigate = useNavigate();
  const [studentsCount, setStudentsCount] = useState(0);
  const [professorsCount, setProfessorsCount] = useState(0);

  /*useEffect(() => {
    const token = localStorage.getItem("accessToken");

    if (!token) {
      navigate("/");
      return;
    }

    const fetchStudents = async () => {
      const response = await fetch("http://localhost:8080/students", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      const data = await response.json();
      setStudentsCount(data.totalElements ?? 0);
    };

    fetchStudents();
  }, [navigate]);*/

  return (
    <DashboardWrapper>
      <Header />

      <MainLayout>
        <Sidebar />

        <Content>
          <CardsGrid>
            <CountCard title="Students" value={studentsCount} />
            <CountCard title="Professors" value={professorsCount} />
            <CountCard title="Classrooms" value={8} />
            <CountCard title="Subjects" value={15} />
          </CardsGrid>
        </Content>
      </MainLayout>
    </DashboardWrapper>
  );
}

export default DashboardPage;