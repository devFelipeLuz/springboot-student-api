import styled from "styled-components";

export const DashboardWrapper = styled.div`
  min-height: 100vh;
  background: linear-gradient(
    135deg,
    #0f172a 0%,
    #1e293b 50%,
    #334155 100%
  );
  color: white;
`;

export const MainLayout = styled.div`
  display: flex;
  min-height: calc(100vh - 70px);
`;

export const Content = styled.main`
  flex: 1;
  padding: 32px;
`;

export const CardsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(2, minmax(240px, 1fr));
  gap: 20px;
`;